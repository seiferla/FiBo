from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from .serializers import FiboUserSerializer
from .models import FiboUser
from django.contrib.auth import authenticate


@api_view(['GET'])
def getUser(request, pk):
    user = FiboUser.objects.get(id=pk)
    serializer = FiboUserSerializer(user, many=False)
    return Response(serializer.data, status=status.HTTP_200_OK)


@api_view(['DELETE'])
def deleteUser(request, pk):
    user = FiboUser.objects.get(id=pk)
    user.delete()
    return Response(f'User with id {pk} was deleted', status=status.HTTP_200_OK)


@api_view(['POST'])
def registerUser(request):
    # Note that username and email are the same
    user = FiboUser.objects.create_user(username=request.data['email'], email=request.data['email'],
                                        password=request.data['password'])
    return Response(f'User with id {user.id} and email {user.email} was created', status=status.HTTP_201_CREATED)


@api_view(['POST'])
def authenticateUser(request):
    # Note that username and email are the same
    user = authenticate(username=request.data['email'], password=request.data['password'])
    if user is not None:
        # TODO login(request,user)
        # redirect to a success page
        return Response(f'Authenticated user {user.email}', status=status.HTTP_200_OK)
    else:
        return Response('Invalid login', status=status.HTTP_401_UNAUTHORIZED)


@api_view(['GET'])
def getRoutes(request):
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
            'Endpoint': '/users/<id>/delete',
            'method': 'DELETE',
            'body': None,
            'description': 'Deletes user with given id'
        },
        {
            'Endpoint': '/users/<id>',
            'method': 'GET',
            'body': None,
            'description': 'Returns user with given id'
        },
        {
            'Endpoint': '/users/<id>/update',
            'method': 'PUT',
            'body': {'email': '', 'newPassword': '', 'oldPassword': ''},
            'description': 'Not implemented yet. Updates user with data sent in put request'
        },
    ]
    return Response(routes)
