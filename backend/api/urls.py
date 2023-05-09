from django.urls import path
from . import views
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

urlpatterns = [
    path('', views.GetRoutes.as_view(), name='routes'),
    path('users/register/', views.RegisterUser.as_view(), name='register'),
    path('users/delete/', views.DeleteUser.as_view(), name='delete_user'),
    path('users/get/', views.GetUser.as_view(), name='get_user'),
    path('users/login/', TokenObtainPairView.as_view(), name='login'),
    path('users/authenticate/', TokenRefreshView.as_view(), name='authenticate'),
    path('cashflows/', views.CashflowsView.as_view(), name='cashflow'),
    path('cashflows/<int:cashflow_id>',
         views.CashflowsView.as_view(), name='cashflow_with_id'),
    path('places/', views.PlaceView.as_view(), name='place'),
    path('categories/', views.CategoryView.as_view(), name='category'),
]
