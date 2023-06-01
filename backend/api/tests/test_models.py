from django.test import TestCase

from ..models import Store, Category, Account, Cashflow, Item, FiboUser, ZipCity


class ModelsTestCase(TestCase):

    def test_place_creation(self):
        zip = ZipCity.objects.create(zip="12345", city="Karlsruhe")
        account = Account.objects.create(name="TestAccount")
        place = Store.objects.create(account=account,street="Kaiserstraße", zip=zip, house_number="12", name="Postgalerie")
        self.assertEqual(place.street, 'Kaiserstraße')
        self.assertEqual(place.house_number, '12')
        self.assertEqual(place.zip.zip, "12345")
        self.assertEqual(place.zip.city, "Karlsruhe")
        self.assertEqual(place.name, "Postgalerie")
        self.assertEqual(place.account.name, "TestAccount")

    def test_category_creation(self):
        account = Account.objects.create(name="TestAccount")
        category = Category.objects.create(name="HEALTH",account=account)
        self.assertEqual(category.name, "HEALTH")

    def test_account_creation(self):
        account = Account.objects.create(name="user@user.mail")
        self.assertEqual(account.name, "user@user.mail")

    def test_item_creation(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        item = Item.objects.create(name="Shampoo", amount="3", cashflow=cashflow, value="4.5")
        self.assertEqual(item.name, "Shampoo")
        self.assertEqual(item.amount, "3")
        self.assertIsNotNone(item.cashflow)
        self.assertNotEquals(item.value, "5.6")

    def test_create_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        self.assertGreater(cashflow.id, 0)
        self.assertIsNotNone(cashflow.category)
        self.assertIsNotNone(cashflow.account)
        self.assertIsNotNone(cashflow.place)
        self.assertEqual(cashflow.overall_value, 100)
        self.assertEqual(cashflow.is_income, True)

    def test_save_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        cashflow.overall_value = 200.00
        cashflow.save()
        self.assertIsNotNone(cashflow.updated)
        self.assertGreater(cashflow.id, 0)
        self.assertIsNotNone(cashflow.category)
        self.assertIsNotNone(cashflow.account)
        self.assertIsNotNone(cashflow.place)
        self.assertEqual(cashflow.overall_value, 200)
        self.assertEqual(cashflow.is_income, True)

    def test_delete_cashflow(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="Test Category")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place,
                                           account=account)
        cashflow.delete()
        self.assertEqual(Cashflow.objects.filter(id=cashflow.id).count(), 0)

    def test_fibo_user_creation(self):
        account = Account.objects.create(name="Test Account")
        user = FiboUser.objects.create_user(username='test user', email='test@fibo.de', password='secure')
        user.account.add(account)
        self.assertIsNotNone(user.account)
