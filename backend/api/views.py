from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.views import APIView
from .serializers import FiboUserSerializer
from .models import FiboUser

class getUser(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self, request):
        usermail = request.user.username
        user = FiboUser.objects.get(email=usermail)
        serializer = FiboUserSerializer(user, many=False)
        return Response(serializer.data, status=status.HTTP_200_OK)


class deleteUser(APIView):
    permission_classes = (IsAuthenticated,)

    def delete(self, request):
        usermail = request.user.username
        user = FiboUser.objects.get(email=usermail)
        user.delete()
        return Response(f'Your user account was deleted', status=status.HTTP_200_OK)


class registerUser(APIView):
    permission_classes = (AllowAny, )

    def post(self, request):
            # Note that username and email are the same
            user = FiboUser.objects.create_user(username=request.data['email'], email=request.data['email'],
                                                password=request.data['password'])
            return Response(f'User with id {user.id} and email {user.email} was created', status=status.HTTP_201_CREATED)


class getRoutes(APIView):
    permission_classes = (IsAuthenticated, )

    def get(self):
        routes = [
            {
                'Endpoint': '/users/register',
                'method': 'POST',
                'body': {'email': '', 'password': ''},
                'description': 'Register Endpoint for a new user!'
            },
            {
                'Endpoint': '/users/authenticate',
                'method': 'POST',
                'body': {'email': '', 'password': ''},
                'description': 'Authenticates user with given email and password'
            },
            {
                'Endpoint': '/users/delete',
                'method': 'DELETE',
                'body': None,
                'description': 'Deletes user with given id'
            },
            {
                'Endpoint': '/users/get',
                'method': 'GET',
                'body': None,
                'description': 'Returns user with given id'
            },
            {
                'Endpoint': '/users/update',
                'method': 'PUT',
                'body': {'email': '', 'newPassword': '', 'oldPassword': ''},
                'description': 'Not implemented yet. Updates current user with data sent in put request'
            },
        ]
        return Response(routes)
