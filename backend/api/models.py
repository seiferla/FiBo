from django.db import models
from django.contrib.auth.models import AbstractUser

# Create your models here
class Place(models.Model):
    place_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    address = models.CharField(max_length=55)
    name = models.CharField(max_length=55)


class Category(models.Model):
    name = models.CharField(max_length=55)


class Account(models.Model):
    created = models.DateTimeField(auto_now=True)
    updated = models.DateTimeField(auto_now_add=True)


class Cashflow(models.Model):
    is_income = models.BooleanField()
    overall_value = models.FloatField()
    created = models.DateTimeField(auto_now=True)
    updated = models.DateTimeField(auto_now_add=True)
    category = models.ForeignKey(Category, on_delete=models.DO_NOTHING)
    place = models.OneToOneField(Place, on_delete=models.DO_NOTHING, blank=True, null=True)
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Item(models.Model):
    name = models.CharField(max_length=55)
    amount = models.FloatField()
    cashflow = models.ForeignKey(Cashflow, on_delete=models.CASCADE, blank=True, null=True)
    value = models.FloatField()


class FiboUser(AbstractUser):
    account = models.ManyToManyField(Account, blank=True)


class RefreshToken(models.Model):
    token = models.TextField(primary_key=True)  # may use JSONField instead
    user = models.ForeignKey(FiboUser, on_delete=models.CASCADE)
