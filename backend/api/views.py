from django.http import JsonResponse
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.views import APIView

from .serializers import FiboUserSerializer, PlaceSerializer, CashflowSerializer, CategorySerializer
from .models import FiboUser, Account, Place, Cashflow, Category
from datetime import datetime


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
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        user = FiboUser.objects.create_user(email=email, password=password)
        default_account = Account.objects.create(name=email)
        user.account.add(default_account)

        return JsonResponse({'success': True, 'user': user.email, 'created': user.date_joined},
                            status=status.HTTP_201_CREATED)


class CashflowsView(APIView):
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            account = request.data['account']
            account_id = Account.objects.get(id=account['id'])
            cashflow_type = request.data['type']
            overall_value = request.data['overallValue']
            timestamp = request.data['timestamp']
            category, _ = Category.objects.get_or_create(name=request.data['category'])
            place = request.data['place']
            place_address, _ = Place.objects.get_or_create(address=place['address'], name=place['name'])
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        if cashflow_type == 'INCOME':
            cashflow = Cashflow.objects.create(account=account_id,
                                               is_income=True,
                                               overall_value=overall_value,
                                               created=timestamp,
                                               category=category,
                                               place=place_address)
        else:
            cashflow = Cashflow.objects.create(account=account_id,
                                               is_income=False,
                                               overall_value=overall_value,
                                               created=timestamp,
                                               category=category,
                                               place=place_address)

        return JsonResponse({'success': True, 'cashflow_id': cashflow.id, 'creation_date': cashflow.created},
                            status=status.HTTP_201_CREATED)

    def get(self, request, cashflow_id):
        try:
            cashflow = Cashflow.objects.get(id=cashflow_id)
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)
        serializer = CashflowSerializer(cashflow, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def delete(self, _, cashflow_id):
        try:
            cashflow = Cashflow.objects.get(id=cashflow_id)
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)
        cashflow.delete()
        return JsonResponse({'success': True, 'cashflow_id': cashflow_id}, status=status.HTTP_200_OK)

    def put(self, request, cashflow_id):
        try:
            cashflow = Cashflow.objects.get(id=cashflow_id)

            cashflow.overall_value = request.data['overallValue']
            cashflow.category, _ = Category.objects.get_or_create(name=request.data['category'])
            place = request.data['place']
            cashflow.place, _ = Place.objects.get_or_create(address=place['address'], name=place['name'])
            cashflow.updated = datetime.now()
            cashflow_type = request.data['type']
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        if cashflow_type == 'INCOME':
            cashflow.is_income = True
        else:
            cashflow.is_income = False

        cashflow.save()

        return JsonResponse({'success': True, 'cashflow_id': cashflow_id}, status=status.HTTP_200_OK)


class PlaceView(APIView):
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            place = Place.objects.create(address=request.data['address'], name=request.data['name'])
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'place': place.name}, status=status.HTTP_201_CREATED)

    def get(self, request):
        try:
            place = Place.objects.get(address=request.GET['address'])
        except:
            return JsonResponse({'success': False}, status=status.HTTP_404_NOT_FOUND)

        serializer = PlaceSerializer(place, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class CategoryView(APIView):

    def post(self, request):
        try:
            category = Category.objects.create(name=request.POST['name'])
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        return JsonResponse({'success': True, 'category_id': category.id}, status=status.HTTP_201_CREATED)

    def get(self, request):
        try:
            category = Category.objects.get(name=request.GET['name'])
        except:
            return JsonResponse({'success': False}, status=status.HTTP_400_BAD_REQUEST)

        serializer = CategorySerializer(category, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class GetRoutes(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self, _):
        routes = [
            {
                'Endpoint': '/users/register',
                'method': 'POST',
                'Token needed': 'false',
                'body': {'email': '', 'password': ''},
                'description': 'Registration requires an email and a password (see: body). Afterwards the entered credentials can be used to log in.'
            },
            {
                'Endpoint': '/users/login',
                'method': 'POST',
                'Token needed': 'false',
                'body': {'email': '', 'password': ''},
                'description': 'To log in, the email and password specified during registration must be sent along. The method returns the Refresh and Access Token.'
            },
            {
                'Endpoint': '/users/authenticate',
                'method': 'POST',
                'Token needed': 'Refresh',
                'body': {'refresh': ''},
                'description': 'To authenticate, a valid refresh token needs to be entered. The method returns an Access Token.'
            },
            {
                'Endpoint': '/users/delete',
                'method': 'DELETE',
                'Token needed': 'Access',
                'body': None,
                'description': 'Deletes user that corresponds to the Access Token send in the header'
            },
            {
                'Endpoint': '/users/get',
                'method': 'GET',
                'Token needed': 'Access',
                'body': None,
                'description': 'Returns user that corresponds to the Access Token send in the header'
            },
            {
                'Endpoint': '/users/update',
                'method': 'PUT',
                'Token needed': 'Access',
                'body': {'email': '', 'newPassword': '', 'oldPassword': ''},
                'description': 'Not implemented yet. Updates current user with data sent in put request'
            },
        ]
        return Response(routes)
