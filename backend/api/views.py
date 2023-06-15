from datetime import datetime

from django.http import JsonResponse
from rest_framework import status
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView

from .models import FiboUser, Account, Store, Cashflow, Category, ZipCity, Private, Item, LiteUser
from .serializers import FiboUserSerializer, CashflowSerializer, CategorySerializer, StoreSerializer, PrivateSerializer, \
    ItemSerializer


class GetUser(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self, request):
        usermail = request.user.email
        user = FiboUser.objects.get(email=usermail)
        serializer = FiboUserSerializer(user, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class DeleteUser(APIView):
    permission_classes = (IsAuthenticated,)

    def delete(self, request):
        usermail = request.user.email
        user = FiboUser.objects.get(email=usermail)
        # All accounts assigned to the user are deleted as well
        # if there are other users for one account, then the account is not deleted
        accounts = Account.objects.filter(fibouser__id=user.id)
        for account in accounts:
            if account.fibouser_set.count() == 1:
                account.delete()
        user.delete()
        return JsonResponse({'success': True, 'user': user.email}, status=status.HTTP_200_OK)


class RegisterUser(APIView):
    permission_classes = (AllowAny,)

    def post(self, request):
        try:
            email = request.data['email']
            password = request.data['password']
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        user = LiteUser.objects.create_user(show_premium_ad=True, email=email, password=password)
        default_account = Account.objects.create(name=email)
        user.account.add(default_account)

        return JsonResponse({'success': True, 'user': user.email, 'created': user.date_joined},
                            status=status.HTTP_201_CREATED)


class CashflowsView(APIView):
    # FIXME: check authorization (check if user has permission to manage the said account id)

    permission_classes = (IsAuthenticated,)

    def post(self, request):
        print("Request:", request)

        try:
            account = Account.objects.get(id=request.data['account']['id'])
            # FIXME: Verify the user may access this account
            cashflow_type = request.data['type']
            overall_value = request.data['overallValue']
            timestamp = request.data['timestamp']
            category, _ = Category.objects.get_or_create(name=request.data['category'], account=account)
            source = self.get_source_from_request(account, request)
        except AttributeError as e:
            print(e.__cause__)
            return JsonResponse({'success': False, 'message': 'Invalid source type'},
                                status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        cashflow = Cashflow.objects.create(account=account,
                                           is_income=cashflow_type == 'INCOME',
                                           overall_value=overall_value,
                                           created=timestamp,
                                           category=category,
                                           source=source)

        return JsonResponse({'success': True, 'cashflow_id': cashflow.id, 'creation_date': cashflow.created},
                            status=status.HTTP_201_CREATED)

    def get(self, request, cashflow_id):
        try:
            # FIXME: Verify the user may access this cashflow (i.e. manages the account of this cashflow)
            cashflow = Cashflow.objects.get(id=cashflow_id)
        except Exception as e:
            # FIXME: Differentiate between ObjectDoesNotExist and a broad Exception
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)
        serializer = CashflowSerializer(cashflow, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def delete(self, _, cashflow_id):
        try:
            # FIXME: Verify the user may access this cashflow (i.e. manages the account of this cashflow)
            cashflow = Cashflow.objects.get(id=cashflow_id)
        except Exception as e:
            # FIXME: Differentiate between ObjectDoesNotExist and a broad Exception
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)
        cashflow.delete()
        return JsonResponse({'success': True, 'cashflow_id': cashflow_id}, status=status.HTTP_200_OK)

    def put(self, request, cashflow_id):
        try:
            # FIXME: Verify the user may access this cashflow (i.e. manages the account of this cashflow)
            cashflow = Cashflow.objects.get(id=cashflow_id)

            cashflow.overall_value = request.data['overallValue']
            cashflow.category, _ = Category.objects.get_or_create(
                name=request.data['category'], defaults={"account": cashflow.account})
            source = self.get_source_from_request(cashflow.account, request)

            cashflow.source = source
            cashflow.updated = datetime.now()
            cashflow_type = request.data['type']
        except AttributeError as e:
            print(e.__cause__)
            return JsonResponse({'success': False, 'message': 'Invalid source type'},
                                status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        if cashflow_type == 'INCOME':
            cashflow.is_income = True
        else:
            cashflow.is_income = False

        cashflow.save()

        return JsonResponse({'success': True, 'cashflow_id': cashflow_id}, status=status.HTTP_200_OK)

    def get_source_from_request(self, account, request):
        source_type = request.data['source_type']
        source = None
        if source_type == 'store':
            source, _ = Store.objects.get_or_create(
                name=request.data['store']['name'],
                street=request.data['store']['street'],
                zip=ZipCity.objects.get(zip=request.data['store']['zip']),
                house_number=request.data['store']['house_number'],
                account=account
            )
        elif source_type == 'private':
            source, _ = Private.objects.get_or_create(
                first_name=request.data['private']['first_name'],
                last_name=request.data['private']['last_name'],
                account=account
            )
        else:
            raise AttributeError("invalid source_type")
        return source


class StoreSourcesView(APIView):
    # FIXME: check authorization (check if user has permission to manage the said cashflow / items)
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            account = Account.objects.get(id=request.data['account'])
            zip = ZipCity.objects.get(zip=request.data['store']['zip'])
            place = Store.objects.create(account=account,
                                         name=request.data['store']['name'], street=request.data['store']['street'],
                                         zip=zip, house_number=request.data['store']['house_number'])
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'place': place.id}, status=status.HTTP_201_CREATED)

    def get(self, request, store_id):
        try:
            store = Store.objects.get(id=store_id)
        except Exception as e:
            # FIXME: Differentiate between ObjectDoesNotExist and a broad Exception
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)

        serializer = StoreSerializer(store, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class CategoryView(APIView):
    # FIXME: check authorization (check if user has permission to manage the said account)
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            account = Account.objects.get(id=request.data['account'])
            category = Category.objects.create(name=request.data['category']['name'], account=account)
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'category_id': category.id}, status=status.HTTP_201_CREATED)

    def get(self, request, category_id):
        # FIXME: Verify the user may access this category (i.e. manages the account of this cashflow)
        try:
            category = Category.objects.get(id=category_id)
        except Exception as e:
            # FIXME: Differentiate between ObjectDoesNotExist and a broad Exception
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        serializer = CategorySerializer(category, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class PrivateSourcesView(APIView):
    # FIXME: check authorization (check if user has permission to manage the said cashflow / items)
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            account = Account.objects.get(id=request.data['account'])
            private = Private.objects.create(account=account, first_name=request.data['private']['first_name'],
                                             last_name=request.data['private']['last_name'])
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'private': private.id}, status=status.HTTP_201_CREATED)

    def get(self, request, private_id):
        try:
            private = Private.objects.get(id=private_id)
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)

        serializer = PrivateSerializer(private, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class ItemView(APIView):
    # FIXME: check authorization (check if user has permission to manage the said cashflow / items)
    permission_classes = (IsAuthenticated,)

    def post(self, request, cashflow_id):
        try:
            cashflow = Cashflow.objects.get(id=cashflow_id)
            item = Item.objects.create(name=request.data['name'], amount=request.data['amount'],
                                       value=request.data['value'],
                                       cashflow=cashflow)
        except Exception as e:
            # Fixme differentiate between other exception types
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'item': item.id}, status=status.HTTP_201_CREATED)

    def get(self, request, cashflow_id, item_id):
        try:
            item = Item.objects.get(id=item_id)
            if item.cashflow.id != cashflow_id:
                raise AttributeError('invalid item and cashflow combination')
        except Exception as e:
            # FIXME: Differentiate between ObjectDoesNotExist and a broad Exception
            print(e.__cause__)
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)

        serializer = ItemSerializer(item, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)
