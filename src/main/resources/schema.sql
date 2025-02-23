-- Enable pgcrypto for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create ENUM types for order and payment status
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'orderstatus') THEN
        CREATE TYPE "OrderStatus" AS ENUM ('PROCESSING', 'COMPLETED', 'FAILED', 'RETURN');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'paymentstatus') THEN
        CREATE TYPE "PaymentStatus" AS ENUM ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'paymenttype') THEN
        CREATE TYPE "PaymentType" AS ENUM ('CASH', 'CARD', 'UPI', 'NETBANKING', 'WALLET');
    END IF;
END $$;

-- Create Users Table
CREATE TABLE IF NOT EXISTS "users" (
    "userid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) UNIQUE NOT NULL,
    "mobnumber" VARCHAR(20) NOT NULL
);

-- Create Orders Table
CREATE TABLE IF NOT EXISTS "orders" (
    "orderid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "userid" UUID NOT NULL REFERENCES "users"("userid") ON DELETE CASCADE,
    "totalamount" DECIMAL(10,2) NOT NULL,
    "status" "OrderStatus" NOT NULL,
    "createat" TIMESTAMP NOT NULL DEFAULT now(),
    "updatedat" TIMESTAMP NOT NULL DEFAULT now()
);

-- Create OrderItems Table
CREATE TABLE IF NOT EXISTS "orderitems" (
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "orderid" UUID NOT NULL REFERENCES "orders"("orderid") ON DELETE CASCADE,
    "itemid" UUID NOT NULL,
    "quantity" INT NOT NULL,
    "itemname" VARCHAR(255) NOT NULL,
    "price" DOUBLE PRECISION NOT NULL;
);

-- Create Payments Table
CREATE TABLE IF NOT EXISTS "payments" (
    "paymentid" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "paymenttype" "PaymentType" NOT NULL,
    "paymentstatus" "PaymentStatus" NOT NULL,
    "userid" UUID NOT NULL REFERENCES "users"("userid") ON DELETE CASCADE,
    "orderid" UUID NOT NULL REFERENCES "orders"("orderid") ON DELETE CASCADE
);
