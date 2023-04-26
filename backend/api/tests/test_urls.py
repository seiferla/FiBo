from django.test import TestCase
from django.urls import resolve,reverse
from rest_framework_simplejwt.views import TokenRefreshView, TokenObtainPairView

from ..views import GetRoutes, RegisterUser, DeleteUser, GetUser, CashflowsView, PlaceView, CategoryView


class TestUrls(TestCase):
    def test_routes_url_resolves(self):
        url = reverse('routes')
        self.assertEqual(resolve(url).func.view_class, GetRoutes)

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
        self.assertEqual(resolve(url).func.view_class, PlaceView)

    def test_place_url_resolves(self):
        url = reverse('place')
        self.assertEqual(resolve(url).func.view_class, PlaceView)

    def test_category_url_resolves(self):
        url = reverse('category')
        self.assertEqual(resolve(url).func.view_class, CategoryView)