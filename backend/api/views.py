from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.views import APIView
from .serializers import FiboUserSerializer
from .models import FiboUser

class GetUser(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self, request):
        usermail = request.user.username
        user = FiboUser.objects.get(email=usermail)
        serializer = FiboUserSerializer(user, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class DeleteUser(APIView):
    permission_classes = (IsAuthenticated,)

    def delete(self, request):
        usermail = request.user.username
        user = FiboUser.objects.get(email=usermail)
        user.delete()
        return Response(f'Your user account was deleted', status=status.HTTP_200_OK)


class RegisterUser(APIView):
    permission_classes = (AllowAny, )

    def post(self, request):
            # Note that username and email are the same
            user = FiboUser.objects.create_user(username=request.data['email'], email=request.data['email'],
                                                password=request.data['password'])
            return Response(f'User with id {user.id} and email {user.email} was created', status=status.HTTP_201_CREATED)


class GetRoutes(APIView):
    permission_classes = (IsAuthenticated, )

    def get(self, _):
        routes = [
            {
                'Endpoint': '/users/register',
                'method': 'POST',
                'Token needed': 'false',
                'body': {'email': '', 'password': ''},
                'description': 'Registration requires an email and a password (see: body). Afterwards the entered credentials can be used to log in.'
            },
            {
                'Endpoint': '/users/login',
                'method': 'POST',
                'Token needed': 'false',
                'body': {'username': '', 'password': ''},
                'description': 'To log in, the username (is the same as email) and password specified during registration must be sent along. The method returns the Refresh and Access Token.'
            },
            {
                'Endpoint': '/users/authenticate',
                'method': 'POST',
                'Token needed': 'Refresh',
                'body': {'refresh': ''},
                'description': 'To authenticate, a valid refresh token needs to be entered. The method returns an Access Token.'
            },
            {
                'Endpoint': '/users/delete',
                'method': 'DELETE',
                'Token needed': 'Access',
                'body': None,
                'description': 'Deletes user that corresponds to the Access Token send in the header'
            },
            {
                'Endpoint': '/users/get',
                'method': 'GET',
                'Token needed': 'Access',
                'body': None,
                'description': 'Returns user that corresponds to the Access Token send in the header'
            },
            {
                'Endpoint': '/users/update',
                'method': 'PUT',
                'Token needed': 'Access',
                'body': {'email': '', 'newPassword': '', 'oldPassword': ''},
                'description': 'Not implemented yet. Updates current user with data sent in put request'
            },
        ]
        return Response(routes)
