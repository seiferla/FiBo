from django.contrib.auth.base_user import BaseUserManager
from django.db import models
from django.contrib.auth.models import AbstractUser


class Account(models.Model):
    name = models.CharField(max_length=64)


class Source(models.Model):
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Private(Source):
    first_name = models.CharField(max_length=64)
    last_name = models.CharField(max_length=64)


class ZipCity(models.Model):
    """
    needed for the Store model to make sure we don't save redundant data
    (functional dependency from Store.source_id to Zip as well as City, but City is functionally dependent on Zip as well)
    """
    zip = models.CharField(max_length=8, primary_key=True)
    city = models.CharField(max_length=64)


class Store(Source):
    name = models.CharField(max_length=64)
    street = models.CharField(max_length=64)
    zip = models.ForeignKey(ZipCity, on_delete=models.CASCADE)
    house_number = models.CharField(max_length=64)


class Category(models.Model):
    name = models.CharField(max_length=64, unique=True)
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Cashflow(models.Model):
    is_income = models.BooleanField()
    overall_value = models.DecimalField(max_digits=6, decimal_places=2)
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    category = models.ForeignKey(Category, on_delete=models.CASCADE)
    source = models.ForeignKey(
        Source, on_delete=models.PROTECT)
    account = models.ForeignKey(Account, on_delete=models.CASCADE)


class Item(models.Model):
    name = models.CharField(max_length=55)
    amount = models.IntegerField()
    cashflow = models.ForeignKey(
        Cashflow, on_delete=models.CASCADE, blank=True, null=True)
    value = models.DecimalField(max_digits=6, decimal_places=2)


class UserManager(BaseUserManager):
    use_in_migrations = True

    def _create_user(self, email, password, **extra_fields):
        if not email:
            raise ValueError('Users require an email field')
        email = self.normalize_email(email)
        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_user(self, email, password=None, **extra_fields):
        extra_fields.setdefault('is_superuser', False)
        return self._create_user(email, password, **extra_fields)

    def create_superuser(self, email, password, **extra_fields):
        extra_fields.setdefault('is_superuser', True)

        if extra_fields.get('is_superuser') is not True:
            raise ValueError('Superuser must have is_superuser=True.')

        return self._create_user(email, password, **extra_fields)


class FiboUser(AbstractUser):
    username = None
    first_name = None
    last_name = None
    is_staff = None
    account = models.ManyToManyField(Account, blank=True)
    email = models.EmailField('email address', unique=True)
    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []


class LiteUser(FiboUser):
    show_premium_ad = models.BooleanField(default=True)


class PremiumUser(FiboUser):
    pay_method = models.CharField(max_length=32)
    subscription_date = models.DateTimeField()
