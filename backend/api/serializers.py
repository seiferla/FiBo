from rest_framework.serializers import ModelSerializer
from .models import Account, Cashflow, Store, Private, Item, FiboUser, Category


class AccountSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = '__all__'


class CashflowSerializer(ModelSerializer):
    class Meta:
        model = Cashflow
        fields = '__all__'


class StoreSerializer(ModelSerializer):
    class Meta:
        model = Store
        fields = '__all__'


class PrivateSerializer(ModelSerializer):
    class Meta:
        model = Private
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
