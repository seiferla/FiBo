import json

from django.test import TestCase
from rest_framework import status
from rest_framework.test import APIClient
from rest_framework_simplejwt.tokens import RefreshToken

from ..models import Account, Cashflow, Category, Store, ZipCity, LiteUser, Private, Item


class ViewsTestCase(TestCase):

    def test_user_get(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get("/users/get/")

        # Then
        self.assertEqual(response.status_code, 200)

    def test_user_delete(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
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
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
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
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user2 = LiteUser.objects.create_user(email='test2@fibo.de', password='secure',
                                             show_premium_ad=True)
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
                                    {"email": "test@fibo.de", "password": "test"})

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
                                    {"password": "test"})

        # Then
        self.assertEqual(response.status_code, 400)

    def test_income_cashflow_post(self):
        # Given
        account = Account.objects.create(name="Test Account")
        zip_city = ZipCity.objects.create(city="SomeCity", zip='76131')
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_income = {
            "category": "HEALTH",
            "overallValue": 26.00,
            "source_type": "store",
            "store": {
                "name": "Media",
                "street": "Test Strasse 20",
                "zip": "76131",
                "house_number": "10"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "INCOME",
            "account": {
                "id": account.id
            }
        }

        # When
        response = client.post("/cashflows/", cashflow_income, format='json')

        # Then
        self.assertEqual(response.status_code, 201)

    def test_expense_cashflow_post(self):
        # Given
        account = Account.objects.create(name="Test Account")
        zip_city = ZipCity.objects.create(city="SomeCity", zip='76131')
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_expense = {
            "category": "HEALTH",
            "overallValue": 26.00,
            "source_type": "store",
            "store": {
                "name": "Media",
                "street": "Test Strasse 20",
                "zip": "76131",
                "house_number": "10"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": account.id
            }
        }

        # When
        response = client.post("/cashflows/", cashflow_expense, format='json')

        # Then
        self.assertEqual(response.status_code, 201)

    def test_cashflow_post_bad_request(self):
        # Given
        account = Account.objects.create(name="Test Account")
        zip_city = ZipCity.objects.create(city="SomeCity", zip='76131')
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        invalid_cashflow = {
            # "category": "HEALTH",
            "overallValue": 26.00,
            "source_type": "store",
            "store": {
                "name": "Media",
                "street": "Test Strasse 20",
                "zip": 76131,
                "house_number": 10
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": "1"
            }
        }
        # When
        response = client.post("/cashflows/", invalid_cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)

    def test_cashflow_get(self):
        # Given
        user = LiteUser.objects.create_user(show_premium_ad=True, email='test@fibo.de',
                                            password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")

        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        user.account.add(account)

        # When
        response = client.get(f'/cashflows/{cashflow.id}/')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.headers['Content-Type'], "application/json")

        json_response = json.loads(response.content)

        self.assertTrue(json_response.items() >= {
            "id": 1,
            "is_income": True,
            "overall_value": "100.00",
            "category": category.id,
            "source": store.id,
            "account": account.id,
        }.items())

    # Try to get a not existing Cashflow
    def test_cashflow_get_bad_parameter(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_id = '0'

        # When
        response = client.get(f'/cashflows/{cashflow_id}/')

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertEqual(response.json(), {'success': False})

    def test_cashflow_delete(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='test',
                                            show_premium_ad=True)

        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")

        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        user.account.add(account)

        # When
        response = client.delete(f'/cashflows/{cashflow.id}/')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {
            'success': True, 'cashflow_id': cashflow.id})

        # The Cashflow should actually be deleted from the database
        with self.assertRaises(Cashflow.DoesNotExist):
            cashflow.refresh_from_db()

        # Ensure the current source still exists
        # in case this throws an error, the source was deleted also when the cashflow got deleted
        # (yet it is expected to survive)
        store.refresh_from_db()

    # Try to delete a not existing Cashflow
    def test_cashflow_delete_bad_parameter(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow_id = '0'

        # When
        response = client.delete(f'/cashflows/{cashflow_id}/')

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertEqual(response.json(), {'success': False})

    def test_cashflow_put_income(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")

        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        user.account.add(account)

        cashflow_id = cashflow.id
        cashflow = {
            "category": "HEALTH",
            "overallValue": 26.00,
            "source_type": "store",
            "store": {
                "name": "Media",
                "street": "Test Strasse 20",
                "zip": 76131,
                "house_number": 10
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "INCOME",
            "account": {
                "id": 1
            }
        }

        # When
        response = client.put(
            f'/cashflows/{cashflow_id}/', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {
            'success': True, 'cashflow_id': cashflow_id})

    def test_cashflow_put_expense(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)
        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        user.account.add(account)

        updated_cashflow = {
            "category": "MOBILITY",
            "overallValue": 26.00,
            "source_type": "private",
            "private": {
                "first_name": "Max",
                "last_name": "Mustermann"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": 1
            }
        }

        # When
        response = client.put(
            f'/cashflows/{cashflow.id}/', updated_cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), {
            'success': True, 'cashflow_id': cashflow.id})

    def test_update_not_existing_cashflow(self):
        # Given
        user = LiteUser.objects.create_user(show_premium_ad=True, email='test@fibo.de',
                                            password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        cashflow = {
            "category": "MOBILITY",
            "overallValue": 26.00,
            "source_type": "private",
            "private": {
                "first_name": "Max",
                "last_name": "Mustermann"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": 1
            }
        }

        # Whe
        response = client.put(f'/cashflows/1337/', cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    # Try to update a Cashflow with insufficient data
    def test_cashflow_put_bad_request(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(id=1, name="Test Account")
        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)

        cashflow = Cashflow.objects.create(id=1, is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)

        new_cashflow = {
            # "category": "MOBILITY",
            "overallValue": 26.00,
            "source_type": "private",
            "private": {
                "first_name": "Max",
                "last_name": "Mustermann"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": 1
            }
        }

        # Whe
        response = client.put(f'/cashflows/{cashflow.id}/', new_cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    # Try to update a Cashflow with wrong source type
    def test_cashflow_put_invalid_source_type_request(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="Health", account=account)
        zip_city = ZipCity.objects.create(zip='76131', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)

        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)

        new_cashflow = {
            "category": "MOBILITY",
            "overallValue": 26.00,
            "source_type": "invalid",
            "private": {
                "first_name": "invalid",
                "last_name": "invalid"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": account.id
            }
        }

        # Whe
        response = client.put(f'/cashflows/{cashflow.id}/', new_cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.json(), {'success': False, 'message': 'Invalid source type'})

    # Try to create a Cashflow with wrong source type
    def test_cashflow_post_invalid_source_type_request(self):
        # Given
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        account = Account.objects.create(name="Test Account")

        new_cashflow = {
            "category": "MOBILITY",
            "overallValue": 26.00,
            "source_type": "invalid",
            "private": {
                "first_name": "invalid",
                "last_name": "invalid"
            },
            "timestamp": "2023-04-23T00:00:00",
            "type": "EXPENSE",
            "account": {
                "id": account.id
            }
        }

        # Whe
        response = client.post(f'/cashflows/', new_cashflow, format='json')

        # Then
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertEqual(response.json(), {'success': False, 'message': 'Invalid source type'})

    def test_private_post(self):
        # Given
        account = Account.objects.create(name="Test Account")

        user = LiteUser.objects.create_user(show_premium_ad=False, email='test@fibo.de',
                                            password='test')
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {
            "private": {
                "first_name": "Max",
                "last_name": "Mustermann"
            },
            "account": account.id
        }

        # When
        response = client.post(f'/sources/privates/', data, format='json')

        private = Private.objects.get(first_name="Max", last_name="Mustermann")

        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.json(), {'success': True, 'private': private.id})

    def test_store_post(self):
        # Given
        account = Account.objects.create(name="Test Account")

        user = LiteUser.objects.create_user(show_premium_ad=False, email='test@fibo.de',
                                            password='test')
        zip_city = ZipCity.objects.create(zip='76131', city="someCity")

        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {
            "store": {
                "name": "Media",
                "street": "Test Strasse 20",
                "zip": 76131,
                "house_number": 10
            },
            "account": account.id
        }

        # When
        response = client.post(f'/sources/stores/', data, format='json')

        store = Store.objects.get(name="Media", zip='76131', house_number="10", street="Test Strasse 20")

        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.json(), {'success': True, 'place': store.id})

    # Try to create a Place with missing address
    def test_store_post_invalid_format(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {
            "store": {
                "name": "invalid store"
            },
            "account": account.id}
        # When
        response = client.post(f'/sources/stores/', data, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    # Try to create a Place with missing address
    def test_private_post_invalid_format(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {
            "private": {
                "first_name": "invalid format"
            },
            "account": account.id}
        # When
        response = client.post(f'/sources/privates/', data, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual(response.json(), {'success': False})

    def test_store_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        zip_city = ZipCity.objects.create(zip='12345', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)

        # When
        response = client.get(f'/sources/stores/{store.id}/')
        json_response = json.loads(response.content)

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertTrue(json_response.items() >= {'id': store.id,
                                                  'name': store.name,
                                                  'street': store.street,
                                                  'house_number': store.house_number,
                                                  'account': account.id,
                                                  'zip': zip_city.zip}.items())

    def test_private_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        private = Private.objects.create(first_name="First name", last_name="Last name", account=account)

        # When
        response = client.get(f'/sources/privates/{private.id}/')
        # json_response = json.loads(response.content)

        # Then
        self.assertEqual(response.status_code, 200)
        # self.assertTrue(json_response.items() >= {'id': private.id,
        #                                           'first_name': private.first_name,
        #                                           'last_name': private.last_name,
        #                                           'account': account.id}.items())

    # Try to get a not existing Place
    def test_store_get_bad_request(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        zip_city = ZipCity.objects.create(zip='12345', city="Karlsruhe")
        store = Store.objects.create(
            name="Test Place", street="Test Street", zip=zip_city, house_number="1", account=account)

        # When
        response = client.get(f'/sources/stores/{1337}/')

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json(), {'success': False})

    # Try to get a not existing Place
    def test_private_get_bad_request(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get(f'/sources/privates/{1337}/')

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertDictEqual(response.json(), {'success': False})

    def test_category_post(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {"category": {"name": "HEALTH"}, "account": account.id}
        # When
        response = client.post(f'/categories/', data, format='json')
        category = Category.objects.get(name="HEALTH", account=account.id)
        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual({'success': True, 'category_id': category.id}, response.json())

    # Try to create a Category without name
    def test_category_post_invalid_format(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        data = {"category": {"invalid": "HEALTH"}, "account": account.id}

        # When
        response = client.post(f'/categories/', data, format='json')
        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual({'success': False}, response.json())

    def test_category_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        category = Category.objects.create(name="HEALTH", account=account)
        # When
        response = client.get(f'/categories/{category.id}/')
        # Then
        self.assertEqual(response.status_code, 200)

    # Try to get not existing Category
    def test_category_get_invalid_parameters(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        # When
        response = client.get(f'/categories/{1337}/')
        # Then
        self.assertEqual(response.status_code, 400)

    def test_item_successful_post(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        category = Category.objects.create(name="HEALTH", account=account)
        private = Private.objects.create(first_name="Max", last_name="Mustermann", account=account)
        cashflow = Cashflow.objects.create(is_income=False, overall_value=20.0, category=category, source=private,
                                           account=account)

        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        item_name = "someItem"
        item_value = 20.2
        item_amount = 3
        item = {
            "name": item_name,
            "value": item_value,
            "amount": item_amount
        }

        # When
        #   ('cashflows/<int:cashflow_id>/items/'),

        response = client.post(f'/cashflows/{cashflow.id}/items/', item, format='json')

        item = Item.objects.get(name=item_name, value=item_value, amount=item_amount)
        # Then
        self.assertEqual(response.status_code, 201)
        self.assertEqual({'success': True, 'item': item.id}, response.json())

    def test_item_post_invalid_format(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        category = Category.objects.create(name="HEALTH", account=account)
        private = Private.objects.create(first_name="Max", last_name="Mustermann", account=account)
        cashflow = Cashflow.objects.create(is_income=False, overall_value=20.0, category=category, source=private,
                                           account=account)

        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
        item_name = "someItem"
        item_value = "12.34"
        item_amount = 3
        item = {
            "invalid": item_name,
            "value": item_value,
            "amount": item_amount
        }

        # When
        response = client.post(f'/cashflows/{cashflow.id}/items/', item, format='json')

        # Then
        self.assertEqual(response.status_code, 400)
        self.assertEqual({'success': False}, response.json())

    def test_item_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        category = Category.objects.create(name="HEALTH", account=account)
        private = Private.objects.create(first_name="Max", last_name="Mustermann", account=account)
        cashflow = Cashflow.objects.create(is_income=False, overall_value=20.0, category=category, source=private,
                                           account=account)
        item_name = "someItem"
        item_value = "12.34"
        item_amount = 3

        item = Item.objects.create(name=item_name, amount=item_amount,
                                   value=item_value,
                                   cashflow=cashflow)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get(f'/cashflows/{cashflow.id}/items/{item.id}/', format='json')
        json_response = json.loads(response.content)

        # Then
        self.assertEqual(response.status_code, 200)
        self.assertTrue(json_response.items() >= {
            "id": item.id,
            "name": item_name,
            "amount": item_amount,
            "value": item_value,
            "cashflow": cashflow.id,
        }.items())

    def test_item_with_wrong_cashflow_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        category = Category.objects.create(name="HEALTH", account=account)
        private = Private.objects.create(first_name="Max", last_name="Mustermann", account=account)
        cashflow = Cashflow.objects.create(is_income=False, overall_value=20.0, category=category, source=private,
                                           account=account)
        item_name = "someItem"
        item_value = "12.34"
        item_amount = 3

        item = Item.objects.create(name=item_name, amount=item_amount,
                                   value=item_value,
                                   cashflow=cashflow)
        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get(f'/cashflows/{1337}/items/{item.id}/', format='json')

        # Then
        self.assertEqual(item, Item.objects.get(id=item.id))
        self.assertEqual(response.status_code, 404)
        self.assertEqual(response.json(), {'success': False})

    def test_item_completely_invalid_get(self):
        # Given
        account = Account.objects.create(name="Test Account")
        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)

        refresh = RefreshToken.for_user(user)
        client = APIClient()
        client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')

        # When
        response = client.get(f'/cashflows/{1337}/items/{1337}/', format='json')
        json_response = json.loads(response.content)

        # Then
        self.assertEqual(response.status_code, 404)
        self.assertEqual(response.json(), {'success': False})
