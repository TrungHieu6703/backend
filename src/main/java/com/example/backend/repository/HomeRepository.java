package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Product;

@Repository
public interface HomeRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        WITH hot_products AS (
            SELECT JSON_OBJECT('id', p.id, 'name', p.name, 'price', p.price, 'coupon', IFNULL(cu.discount, 0), 'specs_summary', p.specs_summary) AS product
            FROM product p
                     LEFT JOIN coupon cu ON p.coupon_id = cu.id
            WHERE p.is_hot = 1
            ORDER BY p.price DESC
            LIMIT 5
        ),
        visible_brands AS (
            SELECT DISTINCT b.id, b.name
            FROM brand b
            WHERE b.display_home = 1
        ),
        visible_products AS (
            SELECT b.name AS brand,
                   JSON_OBJECT('id', p.id, 'name', p.name, 'price', p.price, 'coupon', IFNULL(cu.discount, 0), 'specs_summary', p.specs_summary) AS product
            FROM product p
                     JOIN brand b ON p.brand_id = b.id
                     JOIN visible_brands vb ON b.id = vb.id
                     LEFT JOIN coupon cu ON p.coupon_id = cu.id
            ORDER BY p.price DESC
            LIMIT 5
        ),
        phu_kien AS (
            SELECT JSON_OBJECT('id', p.id, 'name', p.name, 'price', p.price, 'coupon', IFNULL(cu.discount, 0), 'specs_summary', p.specs_summary) AS product
            FROM product p
                     JOIN category c ON p.category_id = c.id
                     LEFT JOIN coupon cu ON p.coupon_id = cu.id
            WHERE c.name = 'Phụ kiện'
            ORDER BY p.price DESC
            LIMIT 5
        ),
        laptop AS (
            SELECT JSON_OBJECT('id', p.id, 'name', p.name, 'price', p.price, 'coupon', IFNULL(cu.discount, 0), 'specs_summary', p.specs_summary) AS product
            FROM product p
                     JOIN category c ON p.category_id = c.id
                     LEFT JOIN coupon cu ON p.coupon_id = cu.id
            WHERE c.name = 'Laptop'
            ORDER BY p.price DESC
            LIMIT 10
        ),
        grouped_data AS (
            SELECT 'hot_products' AS group_type, NULL AS brand, product
            FROM hot_products
            UNION ALL
            SELECT 'visible_products' AS group_type, brand, product
            FROM visible_products
            UNION ALL
            SELECT 'phu_kien' AS group_type, NULL AS brand, product
            FROM phu_kien
            UNION ALL
            SELECT 'laptop' AS group_type, NULL AS brand, product
            FROM laptop
        ),
        hot_products_agg AS (
            SELECT JSON_ARRAYAGG(product) AS hot_products_array
            FROM grouped_data
            WHERE group_type = 'hot_products'
        ),
        visible_products_agg AS (
            SELECT JSON_OBJECTAGG(brand, product) AS visible_products_obj
            FROM grouped_data
            WHERE group_type = 'visible_products' AND brand IS NOT NULL
        ),
        phu_kien_agg AS (
            SELECT JSON_ARRAYAGG(product) AS phu_kien_array
            FROM grouped_data
            WHERE group_type = 'phu_kien'
        ),
        laptop_agg AS (
            SELECT JSON_ARRAYAGG(product) AS laptop_array
            FROM grouped_data
            WHERE group_type = 'laptop'
        )
        SELECT JSON_OBJECT(
            'hot_products', IFNULL(h.hot_products_array, JSON_ARRAY()),
            'brands', IFNULL(v.visible_products_obj, JSON_OBJECT()),
            'phu_kien', IFNULL(p.phu_kien_array, JSON_ARRAY()),
            'laptop', IFNULL(l.laptop_array, JSON_ARRAY())
        ) AS result
        FROM hot_products_agg h, visible_products_agg v, phu_kien_agg p, laptop_agg l
    """, nativeQuery = true)
    String getHomePageData();
}