from django.test import TestCase
from ..models import Place, Category, Account, Cashflow, Item, FiboUser


class ModelsTestCase(TestCase):

    def test_place_creation(self):
        place = Place.objects.create(address="Kaiserstraße 12", name="Postgalerie")
        self.assertEqual(place.name, 'Postgalerie')
        self.assertEqual(place.address, "Kaiserstraße 12")

    def test_category_creation(self):
        category = Category.objects.create(name="HEALTH")
        self.assertEqual(category.name, "HEALTH")

    def test_account_creation(self):
        account = Account.objects.create(name="user@user.mail")
        self.assertEqual(account.name, "user@user.mail")

    def test_item_creation(self):
        account = Account.objects.create(name="Test Account")
        category = Category.objects.create(name="HEALTH")
        place = Place.objects.create(name="Test Place", address="Test Address")
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place, account=account)
        item = Item.objects.create(name="Shampoo", amount="3", cashflow=cashflow, value="4.5")
        self.assertEqual(item.name,"Shampoo")
        self.assertEqual(item.amount,"3")
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
        cashflow = Cashflow.objects.create(is_income=True, overall_value=100.00, category=category, place=place, account=account)
        cashflow.delete()
        self.assertEqual(Cashflow.objects.filter(id=cashflow.id).count(), 0)

    def test_fibo_user_creation(self):
        account = Account.objects.create(name="Test Account")
        user = FiboUser.objects.create_user(username='test@fibo.de', email='test@fibo.de', password='secure')
        user.account.add(account)
        self.assertIsNotNone(user.account)
