# 요구사항
### 기능 요구사항
* 데이터 : 브랜드 - 상품 카테고리별 상품 가격
* 다음의 4가지 api 제공
    1. 카테고리별 최저가 상품의 브랜드 및 가격, 총액 조회 api
    2. 단일 브랜드로 모든 카테고리의 상품을 구매했을때, 상품가격 총합이 최저값인 브랜드의 각 카테고리별 상품 가격 및 총액 조회 api
    3. 특정 상품 카테고리에 대해 최저, 최고 가격의 브랜드와 상품 가격을 조회하는 api
    4. 브랜드 및 상품 Create, Update, Delete api
* api 처리 실패시, 에러 응답과 함께 에러 메시지 함께 응답

### 개발 요구사항
* 로컬 H2DB 사용
* 단위 테스트, 통합 테스트 작성


# 설계
### 1. Brute force
* DB 하나의 테이블로 모두 삽입후 CRUD 수행합니다.
    * Brand (brand_id, brand_name, price_sum), PK(brand_id)
    * Product (product_id, product_name, category_name, brand_name, price), PK(product_id)
* 문제점
    * 데이터 양이 많아지거나, 요청량이 많아지면 DB 에 부하가 가중되어 병목이 발생할 수 있습니다.
* 개선
    * 요구사항을 보면, 최저/최고 가격 조회가 많습니다. 인덱스 생성을 통해 최저/최고 가격 상품 및 브랜드 조회 성능을 개선합니다.

### 2. 인덱스 생성을 통한 성능 향상
* Prodcut 테이블 에 다음의 인덱스들을 추가합니다.
    * idx_product_category_price_brand (category_name, price, brand_name)
    * idx_product_brand_category_price (brand_name, category_name, price)
* Brand 테이블에 다음의 인덱스를 추가합니다.
	* idx_brand_price_sum (price_sum, brand_name)
* Product 테이블 인덱스를 통해 "1. 카테고리별 최저가 상품의 브랜드 및 가격, 총액 조회 api" 를 효율적으로 처리할 수 있습니다.
    ```
    SELECT
        p2.category_name AS categoryName,
        p2.brand_name AS brandName,
        p2.price AS minPrice
    FROM
        (SELECT category_name, MIN(price) AS min_price FROM Product GROUP BY category_name) p1,
        Product p2
    WHERE
        p1.category_name = p2.category_name
    AND
        p1.min_price = p2.price
    ```
    * 서브쿼리는 인덱스를 이용해 루스 인덱스 스캔 + 커버링 인덱스 쿼리로 수행되기에 성능이 좋습니다.
    * join 수행시, 인덱스 사용이 가능한 p1 테이블이 드리븐 테이블이 되어 join 될것이기때문에 성능 저하 적을것으로 예상됩니다.

* 마찬가지로, Product 테이블 인덱스를 통해 "3. 특정 상품 카테고리에 대해 최저, 최고 가격의 브랜드와 상품 가격을 조회"하는 api 를 효율적으로 처리 가능합니다.
    ```
    SELECT
        category_name AS categoryName,
        brand_name AS brandName,
        price AS price
    FROM
        Product
    WHERE
        category_name = #{categoryName}
    ORDER BY
        price
    LIMIT 1
    ```
    * 커버링 인덱스 쿼리 및 스트리밍 처리로 매우 빠르게 조회가 될 것입니다.
* Brand 테이블 인덱스를 통해 "2. 단일 브랜드로 모든 카테고리의 상품을 구매했을때, 상품가격 총합이 최저값인 브랜드의 각 카테고리별 상품 가격 및 총액 조회 api" 를 효율적으로 처리할 수 있습니다.
    ```
    SELECT
        category_name AS categoryName,
        brand_name AS brandName,
        price AS price,
    FROM
        Product
    WHERE
        brand_name = (SELECT brand_name FROM Brand ORDER BY price_sum LIMIT 1)
    ```
    * 서브쿼리는 인덱스를 통해 효율적으로 수행됩니다.
    * 외부쿼리또한 커버링 인덱스 쿼리로 효율적으로 수행됩니다.
* 문제점
    * 조회 요청량이 많아질경우, 아무리 인덱스를 통해 조회 성능을 개선했다 해도 기본적으로 IO 작업으로 인한 여러가지 부하(네트워크 대역폭, DB 서버 부하등)도 증가할것입니다.
* 개선
    * 조회하는 데이터가 한정적이고 자주 바뀌지 않는 특성을 가지고있습니다. 캐싱 적용시 hit rate 이 높을것으로 예상되므로 IO 작업 수행을 많이 줄일 수 있을것입니다.
    * 또는 상품 카테고리 칼럼을 샤드키로 샤딩하여 DB 서버를 scale-out 할 수도 있습니다.

### 3. 로컬 캐시를 활용한 성능 향상
* 데이터의 특성상 캐시 적용시 효율이 높을것으로 보입니다.
    1. 수정보단 조회 요청이 더 많을것으로 예상됩니다.
    2. 따라서 데이터가 자주 바뀌지 않아 cache-hit rate 이 높을것으로 예상됩니다.
    3. 조회하는 데이터 종류가 한정적이라 인메모리 캐시 사용해도 메모리 사용량이 많지 않을것으로 예상됩니다.
* 따라서 로컬 인메모리 캐시를 두고, 조회 요청을 인메모리 캐시에서 처리하게 함으로서 조회시 (DB)IO 작업으로 인한 성능저하를 완화할 수 있습니다.
* 로컬 인메모리에 캐시엔 전체 데이터를 저장하는것이 아닌, 아래의 데이터만 저장합니다. 따라서 메모리 공간 사용량 또한 매우 적을것으로 예상됩니다.
	1. 상품 카테고리별 최저가 브랜드 및 그 가격 정보 (CategoryMinPriceBrandList)
	2. 상품가격 총합이 최저값인 브랜드의 각 카테고리별 상품 가격 정보 (CheapestBrandProductList)
    3. 상품 카테고리별 최저/최고가 브랜드 및 그 가격 정보 (CategoryMinMaxPriceBrand)
* 데이터 수정 요청시, 
    * 전체 캐시를 evict 합니다.
       * 추후 수정된 데이터에 영향을 받는 캐시만 evict 하는 식으로 섬세한 캐시 제어를 통해 개선이 가능합니다.
    * DB 먼저 수정후 DB 수정 성공시에 인메모리 캐시를 수정합니다.
	   * DB 와 인메모리캐시의 데이터 정합성이 중요합니다. 인메모리 캐시 먼저 수정 후 DB update 과정에서 실패하면, 인메모리 캐시와 DB 데이터간 정합성이 깨질 수 있습니다.
	   * 물론 DB 트랜잭션 롤백시, 인메모리 캐시의 데이터도 원래 데이터로 롤백하도록 로직을 구현하면 됩니다. 하지만 롤백하는 사이에 롤백되지 않은 가격정보를 조회해간 사용자가 발생할 수 있습니다.
    
### Future work
* 위 설계는 모두 단일 인스턴스로 서비스가 구동되는 상황을 가정한 설게입니다. 하지만 실제 서비스 환경에선 고가용성 및 처리량을 위해 여러개의 인스턴스로 구성된 클러스터 형태로 서비스가 운영될 것 입니다.
* 멀티 인스턴스인경우, 3번으로 제안한 로컬캐시를 이용한 솔루션은 그대로는 사용할 수 없게됩니다. 따라서 kafka MQ 를 적용하여 해결합니다.
    * 데이터 조회용 클러스터와 데이터 수정용 클러스터를 분리합니다. (조회작업은 많고 수정작업은 적을것이기 때문에(가정) 조회용 클러스터에 좀 더 많은, 좀 더 좋은 인스턴스를 할당합니다.)
    * 데이터 수정용 클러스터는 수정 요청(상품 추가, 상품 가격 수정 등) 수신시, 해당 수정 요청을 kakfa 로 프로듀싱 합니다.
        * 이때, 하나의 브랜드 상품에 대한 모든 수정 요청은 하나의 파티션으로만 프로듀싱 되도록 프로듀싱시 brand_name 를 key 로 전달합니다.
        > 하나의 브랜드 상품에 대한 수정 요청이 RR 로 여러개의 partition 에 프로듀싱 될 경우, 각 파티션의 consumer lag 에 따라 수정 요청 처리 순서가 뒤바뀔 수 있기 떄문입니다.
    * 컨슈머에선 수정 요청 메시지를 컨슈밍하여 수정 내용에 따라 1) DB 수정 2) 조회용 클러스터 인스턴스들의 로컬 인메모리 캐시 수정 순서로 수정을 수행합니다.
    	* 로컬 인메모리 캐시 수정까지 완료한 이후 offset 을 commit 하여 at least once consuming 을 보장합니다. (데이터가 중복으로 수정되어도 데이터 정합성엔 영향 없으니 EOS 까진 필요 없을듯합니다.)


# 빌드 및 실행
* github repository 를 clone 받으신 후, 디렉토리로 이동하셔서 `mvn clean package` 로 jar 를 생성합니다.
* 생성된 jar 를 `java -jar path/to/jar` 로 실행합니다.
    * 본 어플리케이션은 JDK 17 로 개발되었습니다. 따라서 실행하는 환경에 최소 JRE 17 또는 그 이상의 JRE 버전이 설치되어있어야 합니다.
