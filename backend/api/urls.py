from django.urls import path
from . import views
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

urlpatterns = [
    path('', views.getRoutes.as_view()),
    path('users/register/', views.registerUser.as_view()),
    path('users/delete/', views.deleteUser.as_view()),
    path('users/get/', views.getUser.as_view()),
    path('users/login', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('users/authenticate/', TokenRefreshView.as_view(), name='token_refresh'),
]
