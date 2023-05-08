from django.db import models
from django.contrib.auth.models import AbstractUser


# ID is created automatically
class Place(models.Model):
    address = models.CharField(max_length=55, unique=True)
    name = models.CharField(max_length=55)


class Category(models.Model):
    name = models.CharField(max_length=55, unique=True)


class Account(models.Model):
    name = models.CharField(max_length=55)
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)


class Cashflow(models.Model):
    is_income = models.BooleanField()
    overall_value = models.DecimalField(max_digits=6, decimal_places=2)
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    category = models.ForeignKey(Category, on_delete=models.DO_NOTHING)
    place = models.ForeignKey(Place, on_delete=models.DO_NOTHING, blank=True, null=True)
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Item(models.Model):
    name = models.CharField(max_length=55)
    amount = models.IntegerField()
    cashflow = models.ForeignKey(Cashflow, on_delete=models.CASCADE, blank=True, null=True)
    value = models.DecimalField(max_digits=6, decimal_places=2)


class FiboUser(AbstractUser):
    username = None
    first_name = None
    last_name = None
    is_staff = None
    account = models.ManyToManyField(Account, blank=True)


class RefreshToken(models.Model):
    token = models.TextField(primary_key=True)  # may use JSONField instead
    user = models.ForeignKey(FiboUser, on_delete=models.CASCADE)
