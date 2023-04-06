from django.urls import path
from . import views

urlpatterns = [
    path('', views.getRoutes),
    path('users/', views.getUsers),
    path('users/register/', views.registerUser),
    path('users/authenticate/', views.authenticateUser),
    path('users/<int:pk>/delete/', views.deleteUser),
    path('users/<int:pk>/', views.getUser),
]
