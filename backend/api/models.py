from django.db import models

# Create your models here.
class User(models.Model):
    user_id = models.CharField(max_length=55, primary_key=True, unique=True)
    email = models.CharField(max_length=55, unique=True)
    password = models.CharField(max_length=55)


class Account(models.Model):
    account_id = models.CharField(max_length=55, primary_key=True)


class Cashflow(models.Model):
    cashflow_id = models.UUIDField(primary_key=True)
    is_income = models.BooleanField()
    overall_value = models.FloatField()
    timestamp = models.DateTimeField()


class Item(models.Model):
    item_id = models.UUIDField(primary_key=True)
    name = models.CharField(max_length=55)
    amount = models.FloatField()
    value = models.FloatField()


class Category(models.Model):
    category_id = models.UUIDField(primary_key=True)
    name = models.CharField(max_length=55)


class Place(models.Model):
    address = models.CharField(max_length=55, primary_key=True)
    name = models.CharField(max_length=55)
