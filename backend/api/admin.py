from django.contrib import admin

# Register your models here.

from .models import Account, FiboUser, Cashflow, Item, Place, Category

admin.site.register(Account)
admin.site.register(FiboUser)
admin.site.register(Cashflow)
admin.site.register(Item)
admin.site.register(Place)
admin.site.register(Category)