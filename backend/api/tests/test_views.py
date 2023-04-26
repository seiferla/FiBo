from django.test import TestCase
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.test import APIClient
from ..models import FiboUser, Account, Cashflow, Category, Place


class ViewsTestCase(TestCase):

    def test_user_get(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get("/users/get/")

        # Then
        self.assertEqual(response.status_code, 200)

    def test_user_delete(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.delete("/users/delete/")

        # Then
        self.assertEqual(response.status_code, 200)

    def test_register_user_post(self):
        # Given

        # When
        response = self.client.post("/users/register/",
                                    {"username": "test@fibo.de", "email": "test@fibo.de", "password": "test"})

        # Then
        self.assertEqual(response.status_code, 201)

    def test_cashflow_post(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        user.account.add(account)

        cashflow = {
            "category": "MOBILITY",
            "overallValue": 20.00,
            "place": {
                "address": "Test Strasse 20",
                "name": "TestQuelle"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "INCOME",
            "account": {
                "id": "1"
            }
        }
        # When
        response = client.post("/cashflow/", cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 201)

    def test_cashflow_get(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1,name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1,is_income=True, overall_value=100.00, category=category, place=place,
                                       account=account)
        user.account.add(account)

        # When
        response = client.get(f'/cashflow/{cashflow.id}')

        # Then

        self.assertEqual(response.status_code, 200)


    def test_cashflow_delete(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1,name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1,is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        user.account.add(account)
        # When
        response = client.delete(f'/cashflow/{cashflow.id}')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'success':True, 'cashflow_id':cashflow.id})

    def test_cashflow_put(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1,name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1,is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)

        id = cashflow.id
        cashflow = {
            "category": "HEALTH",
            "overallValue": 26.00,
            "place": {
                "address": "Test Strasse 20",
                "name": "Media"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "INCOME",
            "account": {
                "id": 1
            }
        }

        # Whe
        response = client.put(f'/cashflow/{id}', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'success':True, 'cashflow_id':id})

    def test_place_post(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        place ={
            "address": "Test Address",
            "name": "Test name"
        }
        # When
        response = client.post(f'/place/', place, format='json')

        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.json(), {'success': True, 'place': 'Test name'})

    def test_place_get(self):
        # Given
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        place = Place.objects.create(address="Test adress", name="Media")

        # When
        response = client.get(f'/place/?address={place.address}')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json(), {'id': place.id,'name': place.name, 'address': place.address})



