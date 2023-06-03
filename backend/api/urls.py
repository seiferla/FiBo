from django.urls import path
from . import views
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

urlpatterns = [
    path('users/register/', views.RegisterUser.as_view(), name='register'),
    path('users/delete/', views.DeleteUser.as_view(), name='delete_user'),
    path('users/get/', views.GetUser.as_view(), name='get_user'),
    path('users/login/', TokenObtainPairView.as_view(), name='login'),
    path('users/authenticate/', TokenRefreshView.as_view(), name='authenticate'),
    path('cashflows/', views.CashflowsView.as_view(), name='cashflow'),
    path('cashflows/<int:cashflow_id>/',
         views.CashflowsView.as_view(), name='cashflow_with_id'),
    path('sources/privates/', views.PrivateSourcesView.as_view(), name='sources_private'),
    path('sources/privates<int:store_id>/', views.PrivateSourcesView.as_view(), name='sources_private_with_id'),
    path('sources/stores/', views.StoreSourcesView.as_view(), name='sources_store'),
    path('sources/stores/<int:store_id>/',
         views.StoreSourcesView.as_view(), name='sources_store_with_id'),
    path('categories/', views.CategoryView.as_view(), name='category'),
    path('categories/<int:category_id>/', views.CategoryView.as_view(), name='category_with_id'),
    path('items/', views.ItemView.as_view(), name='item'),
    path('items/<int:item_id>/', views.ItemView.as_view(), name='item_with_id')
]
