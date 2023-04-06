from rest_framework.serializers import ModelSerializer
from .models import *


class AccountSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = '__all__'


class CashflowSerializer(ModelSerializer):
    class Meta:
        model = Cashflow
        fields = '__all__'


class PlaceSerializer(ModelSerializer):
    class Meta:
        model = Place
        fields = '__all__'


class ItemSerializer(ModelSerializer):
    class Meta:
        model = Item
        fields = '__all__'


class FiboUserSerializer(ModelSerializer):
    class Meta:
        model = FiboUser
        fields = '__all__'


class CategorySerializer(ModelSerializer):
    class Meta:
        model = Category
        fields = '__all__'


class RefreshTokenSerializer(ModelSerializer):
    class Meta:
        model = RefreshToken
        fields = '__all__'
