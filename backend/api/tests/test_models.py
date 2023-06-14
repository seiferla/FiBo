from django.test import TestCase
from ..models import Store, Category, Account, Cashflow, Item, FiboUser, ZipCity, LiteUser


class ModelsTestCase(TestCase):

    def test_store_creation(self):
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        account = Account.objects.create(name="TestAccount")
        store = Store.objects.create(account=account, street="Kaiserstraße", zip=zip, house_number="12",
                                     name="Postgalerie")
        self.assertEqual(store.street, 'Kaiserstraße')
        self.assertEqual(store.house_number, '12')
        self.assertEqual(store.zip.zip, "12345")
        self.assertEqual(store.zip.city, "Karlsruhe")
        self.assertEqual(store.name, "Postgalerie")
        self.assertEqual(store.account.name, "TestAccount")

    def test_category_creation(self):
        account = Account.objects.create(name="TestAccount")
        category = Category.objects.create(name="HEALTH", account=account)
        self.assertEqual(category.name, "HEALTH")

    def test_account_creation(self):
        account = Account.objects.create(name="user@user.mail")
        self.assertEqual(account.name, "user@user.mail")

    def test_item_creation(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH", account=account)
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        store = Store.objects.create(account=account, street="Kaiserstraße", zip=zip, house_number="12",
                                     name="Postgalerie")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        item = Item.objects.create(name="Shampoo", amount="3", cashflow=cashflow, value="4.5")
        self.assertEqual(item.name, "Shampoo")
        self.assertEqual(item.amount, "3")
        self.assertIsNotNone(item.cashflow)
        self.assertNotEquals(item.value, "5.6")

    def test_create_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH", account=account)
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        store = Store.objects.create(account=account, street="Kaiserstraße", zip=zip, house_number="12",
                                     name="Postgalerie")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        self.assertGreater(cashflow.id, 0)
        self.assertIsNotNone(cashflow.category)
        self.assertIsNotNone(cashflow.account)
        self.assertIsNotNone(cashflow.source)
        self.assertEqual(cashflow.overall_value, 100)
        self.assertEqual(cashflow.is_income, True)

    def test_save_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH", account=account)
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        store = Store.objects.create(account=account, street="Kaiserstraße", zip=zip, house_number="12",
                                     name="Postgalerie")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)
        cashflow.overall_value = 200.00
        cashflow.save()
        self.assertIsNotNone(cashflow.updated)
        self.assertGreater(cashflow.id, 0)
        self.assertIsNotNone(cashflow.category)
        self.assertIsNotNone(cashflow.account)
        self.assertIsNotNone(cashflow.source)
        self.assertEqual(cashflow.overall_value, 200)
        self.assertEqual(cashflow.is_income, True)

    def test_delete_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH", account=account)
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        store = Store.objects.create(account=account, street="Kaiserstraße", zip=zip, house_number="12",
                                     name="Postgalerie")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, source=store,
                                           account=account)

        cashflow.delete()
        # The Cashflow should actually be deleted from the database
        with self.assertRaises(Cashflow.DoesNotExist):
            cashflow.refresh_from_db()

    def test_fibo_user_creation(self):
        account = Account.objects.create(name="Test Account")

        user = LiteUser.objects.create_user(email='test@fibo.de', password='secure',
                                            show_premium_ad=True)
        user.account.add(account)
        self.assertIsNotNone(user.account)

    def test_fibo_user_creation_no_email(self):
        with self.assertRaises(ValueError):
            user = FiboUser.objects._create_user(email=None, password='secure')

    def test_fibo_superuser_creation(self):
        user = FiboUser.objects.create_superuser(email='test@fibo.de', password='secure')
        self.assertEquals(user.is_superuser, True)

    def test_fibo_superuser_is_false_creation(self):
        with self.assertRaises(ValueError):
            user = FiboUser.objects.create_superuser(email='test@fibo.de', password='secure', is_superuser=False)
