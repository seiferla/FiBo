from django.urls import path
from . import views
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

urlpatterns = [
    path('', views.GetRoutes.as_view()),
    path('users/register/', views.RegisterUser.as_view()),
    path('users/delete/', views.DeleteUser.as_view()),
    path('users/get/', views.GetUser.as_view()),
    path('users/login', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('users/authenticate/', TokenRefreshView.as_view(), name='token_refresh'),
]
