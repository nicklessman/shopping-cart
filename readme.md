# shopping-cart

Shopping Cart is service application you can process your Shopping Cart operations like total price calculation, delivery price calculation, apply coupon or campaign discount.

### Requirements
Java 1.8 or newer

## Build
If you don't have maven installed you can use script:

    ./mvnw clean install

## Usage

### Create and retrieve Shopping Cart information.

#### Request

`POST /v1/shopping-cart`

    curl --header "Content-Type: application/json" --request POST --data '{ "productIdToQuantity" : { "1" : 3, "2" : 4 }, "couponId": 1 }' http://localhost:8080/v1/shopping-cart

```json
{
    "productIdToQuantity" : 
    { 
        "1" : 3,
        "2" : 4
    },
    "couponId": 1
}
```

#### Response
    HTTP/1.1 200 
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 28 Sep 2020 15:06:26 GMT

```json
{
    "totalAmount": "31.05000000000001",
    "deliveryAmount": "9.0",
    "appliedCampaign": "%50 SALE",
    "appliedCoupon": "250 TL GIFT!",
    "productList": [
        {
            "title": "LAMB",
            "price": "50.9",
            "quantity": "4"
        },
        {
            "title": "MILLA HODIE SWEATER",
            "price": "119.5",
            "quantity": "3"
        }
    ]
}
```

### Dummy Data
Dummy data is being inserted via data.sql under resources folder.

Delivery cost per product and cost per delivery defined in the ShoppingCartConstanst.java configuration file as

```java
COST_PER_DELIVERY = 2.5;
COST_PER_PRODUCT = 2.0;
```

#### Categories
| id | title    | parent_id |
|----|----------|-----------|
| 1  | CLOTHING |           |
| 2  | SWEATER  | 1         |
| 3  | HOME     |           |

#### Products
| id | title               | price  | category_id |
|----|---------------------|--------|-------------|
| 1  | MILLA HODIE SWEATER | 119.50 | 2           |
| 2  | LAMB                | 50.90  | 3           |

#### Campaigns
| id | title    | category_id | #min_cart_product | disc_type | disc_amount |
|----|----------|-------------|-------------------|-----------|-------------|
| 1  | %50 SALE | 1           | 1                 | 'rate'    | 50          |

#### Coupons
| id | title        | min_cart_amount | disc_type | disc_amount |
|----|--------------|-----------------|-----------|-------------|
| 1  | 250 TL GIFT! | 280             | 'amount'  | 250         |

