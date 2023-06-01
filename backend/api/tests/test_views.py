from django.test import TestCase
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.test import APIClient
from ..models import FiboUser, Account, Cashflow, Category, Place

testPassword = 'test'

class ViewsTestCase(TestCase):

    def test_get_routes(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        expectet_result = [
            {
                "Endpoint": "/users/register",
                "method": "POST",
                "Token needed": "false",
                "body": {
                    "email": "",
                    "password": ""
                },
                "description": "Registration requires an email and a password (see: body). Afterwards the entered credentials can be used to log in."
            },
            {
                "Endpoint": "/users/login",
                "method": "POST",
                "Token needed": "false",
                "body": {
                    "email": "",
                    "password": ""
                },
                "description": "To log in, the email and password specified during registration must be sent along. The method returns the Refresh and Access Token."
            },
            {
                "Endpoint": "/users/authenticate",
                "method": "POST",
                "Token needed": "Refresh",
                "body": {
                    "refresh": ""
                },
                "description": "To authenticate, a valid refresh token needs to be entered. The method returns an Access Token."
            },
            {
                "Endpoint": "/users/delete",
                "method": "DELETE",
                "Token needed": "Access",
                "body": None,
                "description": "Deletes user that corresponds to the Access Token send in the header"
            },
            {
                "Endpoint": "/users/get",
                "method": "GET",
                "Token needed": "Access",
                "body": None,
                "description": "Returns user that corresponds to the Access Token send in the header"
            },
            {
                "Endpoint": "/users/update",
                "method": "PUT",
                "Token needed": "Access",
                "body": {
                    "email": "",
                    "newPassword": "",
                    "oldPassword": ""
                },
                "description": "Not implemented yet. Updates current user with data sent in put request"
            }
        ]

        # When
        response = client.get("/")

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), expectet_result)

    def test_user_get(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get("/users/get/")

        # Then
        self.assertEqual(response.status_code, 200)

    def test_user_delete(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.delete("/users/delete/")

        # Then
        self.assertEqual(response.status_code, 200)

    # If there is only one User with the same Account, the Account will be deleted too if the User gets deleted
    def test_user_delete_account_delete(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        user.account.add(account)

        # When

        # Then
        self.assertEqual(account.fibouser_set.count(), 1)

        # When
        response = client.delete("/users/delete/")

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(account.fibouser_set.count(), 0)

    # If there are multiple Users with the same Account, the account wont be deleted if one user gets deleted
    def test_user_delete_account_stays(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        user2 = FiboUser.objects.create_user(email='test2@fibo.de', password='test2')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        user.account.add(account)
        user2.account.add(account)

        # When

        # Then
        self.assertEqual(account.fibouser_set.count(), 2)

        # When
        response = client.delete("/users/delete/")

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(account.fibouser_set.count(), 1)

    def test_register_user_post(self):
        # Given

        # When
        response = self.client.post("/users/register/",
                                    {"email": "test@fibo.de", "password": testPassword})

        # Then
        self.assertEqual(response.status_code, 201)

    # Try to register a user without password and without email
    def test_register_user_post_bad_request(self):
        # Given

        # When (password missing)
        response = self.client.post("/users/register/",
                                    {"email": "test@fibo.de"})

        # Then
        self.assertEqual(response.status_code, 400)

        # When (email missing)
        response = self.client.post("/users/register/",
                                    {"password": testPassword})

        # Then
        self.assertEqual(response.status_code, 400)

    def test_cashflow_post(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        user.account.add(account)

        cashflow_income = {
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

        cashflow_expense = {
            "category": "MOBILITY",
            "overallValue": 35.00,
            "place": {
                "address": "Test Strasse 15",
                "name": "TestQuelle"
            },
            "timestamp": "2023-06-05T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": "1"
            }
        }

        # When
        response = client.post("/cashflow/", cashflow_income, format='json')

        # Then
        self.assertEqual(response.status_code, 201)

        # When
        response = client.post("/cashflow/", cashflow_expense, format='json')

        # Then
        self.assertEqual(response.status_code, 201)

    def test_cashflow_post_bad_request(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        user.account.add(account)

        cashflow = {
            "overallValue": 20.00,
            "place": {
                "address": "Test Strasse 20",
                "name": "TestQuelle"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": "1"
            }
        }
        # When
        response = client.post("/cashflow/", cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)

    def test_cashflow_get(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        user.account.add(account)

        # When
        response = client.get(f'/cashflow/{cashflow.id}')

        # Then
        self.assertEqual(response.status_code, 200)

    # Try to get a not existing Cashflow
    def test_cashflow_get_bad_parameter(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_id = '0'

        # When
        response = client.get(f'/cashflow/{cashflow_id}')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    def test_cashflow_delete(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        user.account.add(account)
        # When
        response = client.delete(f'/cashflow/{cashflow.id}')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'success': True, 'cashflow_id': cashflow.id})

    # Try to delete a not existing Cashflow
    def test_cashflow_delete_bad_parameter(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_id = '0'

        # When
        response = client.delete(f'/cashflow/{cashflow_id}')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    def test_cashflow_put(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)

        cashflow_id = cashflow.id
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

        # When
        response = client.put(f'/cashflow/{cashflow_id}', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'success': True, 'cashflow_id': cashflow_id})

        # Given (Expense
        cashflow2 = {
            "category": "MOBILITY",
            "overallValue": 26.00,
            "place": {
                "address": "Test Strasse 20",
                "name": "Media"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": 1
            }
        }

        # When
        response = client.put(f'/cashflow/{cashflow_id}', cashflow2, format='json')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {'success': True, 'cashflow_id': cashflow_id})

    # Try to update a not existing Cashflow
    def test_cashflow_put_bad_parameter(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)

        cashflow_id = '0'
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
        response = client.put(f'/cashflow/{cashflow_id}', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    # Try to update a Cashflow with insufficient data
    def test_cashflow_put_bad_request(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)

        cashflow_id = cashflow.id
        cashflow = {
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
        response = client.put(f'/cashflow/{cashflow_id}', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    def test_place_post(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        place = {
            "address": "Test Address",
            "name": "Test name"
        }
        # When
        response = client.post(f'/place/', place, format='json')

        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.json(), {'success': True, 'place': 'Test name'})

    # Try to create a Place with missing address
    def test_place_post_bad_request(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        place = {
            "name": "Test name"
        }
        # When
        response = client.post(f'/place/', place, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    def test_place_get(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        place = Place.objects.create(address="Test address", name="Media")

        # When
        response = client.get(f'/place/?address={place.address}')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertDictEqual(response.json(), {'id': place.id, 'name': place.name, 'address': place.address})

    # Try to get a not existing Place
    def test_place_get_bad_request(self):
        # Given
        user = FiboUser.objects.create_user(email='test@fibo.de', password=testPassword)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        address = 'unknown address'
        # When
        response = client.get(f'/place/?address={address}')

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json(), {'success': False})

    def test_category_post(self):
        # Given
        category = {"name": "HEALTH"}
        # When
        response = self.client.post(f'/category/', category, format='json')
        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual({'success': True, 'category_id': 14}, response.json())

    # Try to create a Category without name
    def test_category_post_bad_request(self):
        # Given
        category = { }
        # When
        response = self.client.post(f'/category/', category, format='json')
        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual({'success': False}, response.json())

    def test_category_get(self):
        # Given
        category = Category.objects.create(name="HEALTH")
        # When
        response = self.client.get(f'/category/?name={category.name}')
        # Then
        self.assertEqual(response.status_code, 200)

    # Try to get not existing Category
    def test_category_get_bad_parameter(self):
        # Given
        category_name = 'imaginary'
        # When
        response = self.client.get(f'/category/?name={category_name}')
        # Then
        self.assertEqual(response.status_code, 400)


