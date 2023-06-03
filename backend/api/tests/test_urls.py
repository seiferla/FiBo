from django.test import TestCase
from django.urls import resolve, reverse
from rest_framework_simplejwt.views import TokenRefreshView, TokenObtainPairView

from ..views import RegisterUser, DeleteUser, GetUser, CashflowsView, CategoryView, StoreSourcesView, PrivateSourcesView


class TestUrls(TestCase):

    def test_register_url_resolves(self):
        url = reverse('register')
        self.assertEqual(resolve(url).func.view_class, RegisterUser)

    def test_delete_user_url_resolves(self):
        url = reverse('delete_user')
        self.assertEqual(resolve(url).func.view_class, DeleteUser)

    def test_get_user_url_resolves(self):
        url = reverse('get_user')
        self.assertEqual(resolve(url).func.view_class, GetUser)

    def test_login_url_resolves(self):
        url = reverse('login')
        self.assertEqual(resolve(url).func.view_class, TokenObtainPairView)

    def test_authenticate_url_resolves(self):
        url = reverse('authenticate')
        self.assertEqual(resolve(url).func.view_class, TokenRefreshView)

    def test_cashflow_url_resolves(self):
        url = reverse('cashflow')
        self.assertEqual(resolve(url).func.view_class, CashflowsView)

    def test_cashflow_with_id_url_resolves(self):
        url = reverse('cashflow_with_id', args=[1])
        self.assertEqual(resolve(url).func.view_class, CashflowsView)

    def test_store_url_resolves(self):
        url = reverse('sources_store')
        self.assertEqual(resolve(url).func.view_class, StoreSourcesView)

    def test_private_url_resolves(self):
        url = reverse('sources_private')
        self.assertEqual(resolve(url).func.view_class, PrivateSourcesView)

    def test_category_url_resolves(self):
        url = reverse('category')
        self.assertEqual(resolve(url).func.view_class, CategoryView)
