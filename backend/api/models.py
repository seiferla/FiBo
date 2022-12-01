import uuid
from django.db import models
from django.utils import timezone


# Create your models here
class Place(models.Model):
    address = models.CharField(max_length=55, primary_key=True)
    name = models.CharField(max_length=55)


class Category(models.Model):
    category_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=55)


class Account(models.Model):
    account_id = models.CharField(max_length=55, primary_key=True)


class Cashflow(models.Model):
    cashflow_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    is_income = models.BooleanField()
    overall_value = models.FloatField()
    timestamp = models.DateTimeField(default=timezone.now)
    category = models.ForeignKey(Category, on_delete=models.DO_NOTHING)
    place = models.OneToOneField(Place, on_delete=models.DO_NOTHING, blank=True, null=True)
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Item(models.Model):
    item_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=55)
    amount = models.FloatField()
    cashflow = models.ForeignKey(Cashflow, on_delete=models.CASCADE, blank=True, null=True)
    value = models.FloatField()


class User(models.Model):
    user_id = models.CharField(max_length=55, primary_key=True, unique=True)
    email = models.CharField(max_length=55, unique=True)
    password = models.CharField(max_length=55)
    account = models.ManyToManyField(Account, blank=True)


class RefreshToken(models.Model):
    token = models.TextField(primary_key=True)  # may use JSONField instead
    user = models.ForeignKey(User, on_delete=models.CASCADE)
