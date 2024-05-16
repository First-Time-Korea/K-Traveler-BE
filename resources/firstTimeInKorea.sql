create database firstTimeInKorea;
use firstTimeInKorea;


-- 시도 정보 --
CREATE TABLE `sido` (
  `sido_code` int NOT NULL,
  `sido_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`sido_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 구군 코드--
CREATE TABLE `gugun` (
  `gugun_code` int NOT NULL,
  `gugun_name` varchar(30) DEFAULT NULL,
  `sido_code` int NOT NULL,
  PRIMARY KEY (`gugun_code`,`sido_code`),
  KEY `gugun_to_sido_sido_code_fk_idx` (`sido_code`),
  CONSTRAINT `gugun_to_sido_sido_code_fk` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`sido_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 관광지 정보 (기본) --
CREATE TABLE `attraction_info` (
  `content_id` int NOT NULL,
  `sido_code` int DEFAULT NULL,
  `gugun_code` int DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `addr1` varchar(100) DEFAULT NULL,
  `addr2` varchar(50) DEFAULT NULL,
  `zipcode` varchar(50) DEFAULT NULL,
  `tel` varchar(50) DEFAULT NULL,
  `first_image` varchar(200) DEFAULT NULL,
  `first_image2` varchar(200) DEFAULT NULL,
  `readcount` int DEFAULT NULL,
  `latitude` decimal(20,17) DEFAULT NULL,
  `longitude` decimal(20,17) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `attraction_to_sido_code_fk_idx` (`sido_code`),
  KEY `attraction_to_gugun_code_fk_idx` (`gugun_code`),
  CONSTRAINT `attraction_to_gugun_code_fk` FOREIGN KEY (`gugun_code`) REFERENCES `gugun` (`gugun_code`),
  CONSTRAINT `attraction_to_sido_code_fk` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`sido_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 테마 분류 --
CREATE TABLE `theme` (
  `theme_code` varchar(3) NOT NULL,
  `theme_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`theme_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 카테고리 분류 --
CREATE TABLE `category` (
  `category_code` varchar(5) NOT NULL,
  `category_name` varchar(50) DEFAULT NULL,
  `theme_code` varchar(3) NOT NULL,
  PRIMARY KEY (`category_code`,`theme_code`),
  KEY `category_to_theme_theme_code_fk_idx` (`theme_code`),
  CONSTRAINT `category_to_theme_category_code_fk` FOREIGN KEY (`theme_code`) REFERENCES `theme` (`theme_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 관광지 상세 정보 (카테고리 분류) --
CREATE TABLE `attraction_detail` (
  `content_id` int NOT NULL,
  `theme_code` varchar(3) NOT NULL,
  `category_code` varchar(5) NOT NULL,
  `created_time` varchar(14) DEFAULT NULL,
  `modified_time` varchar(14) DEFAULT NULL,
  `booktour` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `attraction_to_theme_code_fk_idx` (`theme_code`),
  KEY `attraction_to_category_code_fk_idx` (`category_code`),
  CONSTRAINT `attraction_to_theme_code_fk` FOREIGN KEY (`theme_code`) REFERENCES `theme` (`theme_code`),
  CONSTRAINT `attraction_to_category_code_fk` FOREIGN KEY (`category_code`) REFERENCES `category` (`category_code`),
  CONSTRAINT `attraction_detail_to_basic_content_id_fk` FOREIGN KEY (`content_id`) REFERENCES `attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 관광지 정의 및 설명 --
CREATE TABLE `attraction_description` (
  `content_id` int NOT NULL,
  `homepage` varchar(100) DEFAULT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  `telname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_attraciton_id_fk` FOREIGN KEY (`content_id`) REFERENCES `attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 회원 --
CREATE TABLE `member` (
	`id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email_id` VARCHAR(50) NOT NULL,
    `email_domain` VARCHAR(100) NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 1,
    `token` VARCHAR(255) NULL,
	PRIMARY KEY (`id`)
)
ENGINE = InnoDB
DEFAULT CHARSET=utf8mb4;

-- 여행 계획 --
CREATE TABLE `plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `member_id` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `plan_to_member_id_fk` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 여행 계획 및 관광지 관계 테이블 --
CREATE TABLE `plan_and_attraction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int DEFAULT NULL,
  `content_id` int DEFAULT NULL,
  `date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `plan_and_attraction_to_plan_id_fk` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`),
  CONSTRAINT `plan_and_attraction_to_attraction_info_fk_id_fk` FOREIGN KEY (`content_id`) REFERENCES `attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

-- 여행 계획 메모 --
CREATE TABLE `plan_memo` (
  `id` int NOT NULL,
  `plan_and_attraction_id` int NOT NULL,
  `text` varchar(1000) DEFAULT NULL,
  `modified_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   CONSTRAINT `plan_memo_to_plan_and_attraction_id_fk` FOREIGN KEY (`plan_and_attraction_id`) REFERENCES `plan_and_attraction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 여행 계획 썸네일 --
CREATE TABLE `plan_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int NOT NULL,
  `save_folder` varchar(45) NOT NULL, -- 파일을 등록할 때만 필드가 추가된다.
  `origin_file` varchar(50) NOT NULL,
  `save_file` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `plan_file_to_plan_plan_id_fk` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 여행지 후기 --
CREATE TABLE `article` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `member_id` VARCHAR(50) NOT NULL,
    `content` VARCHAR(2000) NOT NULL,
    `hit` INT NULL DEFAULT 0,
    `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `modified_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `article_to_member_member_id_fk`
        FOREIGN KEY (`member_id`)
        REFERENCES `member` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
    )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4;

-- 여행지 후기 태그 --
CREATE TABLE `article_tag` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`id`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4;

-- 여행지 후기 사진 --
CREATE TABLE `article_file` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `article_id` INT NOT NULL,
    `save_folder` VARCHAR(45) NOT NULL,
    `origin_file` VARCHAR(50) NOT NULL,
    `save_file` VARCHAR(50) NOT NULL,
	PRIMARY KEY(`id`),
    CONSTRAINT `article_file_to_article_id_fk`
		FOREIGN KEY (`article_id`)
		REFERENCES `article` (`id`)
        ON DELETE CASCADE
		ON UPDATE CASCADE
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4;

-- 여행지 후기 댓글 --
CREATE TABLE `article_comment` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`article_id` INT NOT NULL,
	`member_id` VARCHAR(50) NOT NULL,
	`parent_article_comment_id` INT DEFAULT 0,
    `depth` INT DEFAULT 0,
	`content` VARCHAR(1000) NOT NULL,
	`created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `status` TINYINT NOT NULL DEFAULT 1,
	PRIMARY KEY (`id`),
	CONSTRAINT `article_comment_to_article_id_fk`
		FOREIGN KEY (`article_id`)
		REFERENCES `article` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT `article_comment_to_member_id_fk`
		FOREIGN KEY (`member_id`)
		REFERENCES `member` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `article_comment_to_article_comment_id_fk`
		FOREIGN KEY (`parent_article_comment_id`)
		REFERENCES `article_comment` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET=utf8mb4;

-- 여행지 후기 좋아요 --
CREATE TABLE `article_like` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`article_id` INT NOT NULL,
	`member_id` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `article_like_to_article_id_fk`
		FOREIGN KEY (`article_id`)
		REFERENCES `article` (`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT `article_like_to_member_id_fk`
		FOREIGN KEY(`member_id`)
        REFERENCES `member` (`id`)
        ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET=utf8mb4;

-- 회원 프로필 이미지 --
CREATE TABLE `member_file` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `member_id` VARCHAR(50) NOT NULL,
    `save_folder` VARCHAR(45) NOT NULL,
    `origin_file` VARCHAR(50) NOT NULL,
    `save_file` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
    CONSTRAINT `member_file_to_member_id`
		FOREIGN KEY (`member_id`)
        REFERENCES `member` (`id`)
        ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET=utf8mb4;

-- 여행지 북마크 --
CREATE TABLE `bookamrk` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `member_id` VARCHAR(50) NOT NULL,
    `content_id` INT NOT NULL,
    PRIMARY KEY(`id`),
    CONSTRAINT `bookmark_to_member_id_fk`
		FOREIGN KEY (`member_id`)
        REFERENCES `member` (`id`)
        ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `bookmark_to_attraction_content_id_fk`
		FOREIGN KEY(`content_id`)
        REFERENCES `attraction_info` (`content_id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET=utf8mb4;

-- 여행지 후기 태그 & 여행지 후기 --
CREATE TABLE `article_and_article_tag` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`article_id` INT NOT NULL,
    `article_tag_id` INT NOT NULL,
	PRIMARY KEY(`id`),
    CONSTRAINT `article_and_article_tag_article_id_fk`
		FOREIGN KEY (`article_id`)
        REFERENCES `article` (`id`)
        ON DELETE CASCADE
		ON UPDATE CASCADE,
	CONSTRAINT `article_and_article_tag_article_tag_id_fk`
		FOREIGN KEY (`article_tag_id`)
        REFERENCES `article_tag` (`id`)
        ON DELETE CASCADE
		ON UPDATE CASCADE
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4;


-- ---------------------
-- 시도 초기화 --
LOCK TABLES `sido` WRITE;
INSERT INTO `sido` VALUES (1,'서울'),(2,'인천'),(3,'대전'),(4,'대구'),(5,'광주'),(6,'부산'),(7,'울산'),(8,'세종특별자치시'),(31,'경기도'),(32,'강원도'),(33,'충청북도'),(34,'충청남도'),(35,'경상북도'),(36,'경상남도'),(37,'전라북도'),(38,'전라남도'),(39,'제주도');
UNLOCK TABLES;

-- 구군 초기화 --
LOCK TABLES `gugun` WRITE;
INSERT INTO `gugun` VALUES (1,'강남구',1),(1,'강화군',2),(1,'대덕구',3),(1,'남구',4),(1,'광산구',5),(1,'강서구',6),(1,'중구',7),(1,'세종특별자치시',8),(1,'가평군',31),(1,'강릉시',32),(1,'괴산군',33),(1,'공주시',34),(1,'경산시',35),(1,'거제시',36),(1,'고창군',37),(1,'강진군',38),(1,'남제주군',39),(2,'강동구',1),(2,'계양구',2),(2,'동구',3),(2,'달서구',4),(2,'남구',5),(2,'금정구',6),(2,'남구',7),(2,'고양시',31),(2,'고성군',32),(2,'단양군',33),(2,'금산군',34),(2,'경주시',35),(2,'거창군',36),(2,'군산시',37),(2,'고흥군',38),(2,'북제주군',39),(3,'강북구',1),(3,'미추홀구',2),(3,'서구',3),(3,'달성군',4),(3,'동구',5),(3,'기장군',6),(3,'동구',7),(3,'과천시',31),(3,'동해시',32),(3,'보은군',33),(3,'논산시',34),(3,'고령군',35),(3,'고성군',36),(3,'김제시',37),(3,'곡성군',38),(3,'서귀포시',39),(4,'강서구',1),(4,'남동구',2),(4,'유성구',3),(4,'동구',4),(4,'북구',5),(4,'남구',6),(4,'북구',7),(4,'광명시',31),(4,'삼척시',32),(4,'영동군',33),(4,'당진시',34),(4,'구미시',35),(4,'김해시',36),(4,'남원시',37),(4,'광양시',38),(4,'제주시',39),(5,'관악구',1),(5,'동구',2),(5,'중구',3),(5,'북구',4),(5,'서구',5),(5,'동구',6),(5,'울주군',7),(5,'광주시',31),(5,'속초시',32),(5,'옥천군',33),(5,'보령시',34),(5,'군위군',35),(5,'남해군',36),(5,'무주군',37),(5,'구례군',38),(6,'광진구',1),(6,'부평구',2),(6,'서구',4),(6,'동래구',6),(6,'구리시',31),(6,'양구군',32),(6,'음성군',33),(6,'부여군',34),(6,'김천시',35),(6,'마산시',36),(6,'부안군',37),(6,'나주시',38),(7,'구로구',1),(7,'서구',2),(7,'수성구',4),(7,'부산진구',6),(7,'군포시',31),(7,'양양군',32),(7,'제천시',33),(7,'서산시',34),(7,'문경시',35),(7,'밀양시',36),(7,'순창군',37),(7,'담양군',38),(8,'금천구',1),(8,'연수구',2),(8,'중구',4),(8,'북구',6),(8,'김포시',31),(8,'영월군',32),(8,'진천군',33),(8,'서천군',34),(8,'봉화군',35),(8,'사천시',36),(8,'완주군',37),(8,'목포시',38),(9,'노원구',1),(9,'옹진군',2),(9,'사상구',6),(9,'남양주시',31),(9,'원주시',32),(9,'청원군',33),(9,'아산시',34),(9,'상주시',35),(9,'산청군',36),(9,'익산시',37),(9,'무안군',38),(10,'도봉구',1),(10,'중구',2),(10,'사하구',6),(10,'동두천시',31),(10,'인제군',32),(10,'청주시',33),(10,'성주군',35),(10,'양산시',36),(10,'임실군',37),(10,'보성군',38),(11,'동대문구',1),(11,'서구',6),(11,'부천시',31),(11,'정선군',32),(11,'충주시',33),(11,'예산군',34),(11,'안동시',35),(11,'장수군',37),(11,'순천시',38),(12,'동작구',1),(12,'수영구',6),(12,'성남시',31),(12,'철원군',32),(12,'증평군',33),(12,'천안시',34),(12,'영덕군',35),(12,'의령군',36),(12,'전주시',37),(12,'신안군',38),(13,'마포구',1),(13,'연제구',6),(13,'수원시',31),(13,'춘천시',32),(13,'청양군',34),(13,'영양군',35),(13,'진주시',36),(13,'정읍시',37),(13,'여수시',38),(14,'서대문구',1),(14,'영도구',6),(14,'시흥시',31),(14,'태백시',32),(14,'태안군',34),(14,'영주시',35),(14,'진해시',36),(14,'진안군',37),(15,'서초구',1),(15,'중구',6),(15,'안산시',31),(15,'평창군',32),(15,'홍성군',34),(15,'영천시',35),(15,'창녕군',36),(16,'성동구',1),(16,'해운대구',6),(16,'안성시',31),(16,'홍천군',32),(16,'계룡시',34),(16,'예천군',35),(16,'창원시',36),(16,'영광군',38),(17,'성북구',1),(17,'안양시',31),(17,'화천군',32),(17,'울릉군',35),(17,'통영시',36),(17,'영암군',38),(18,'송파구',1),(18,'양주시',31),(18,'횡성군',32),(18,'울진군',35),(18,'하동군',36),(18,'완도군',38),(19,'양천구',1),(19,'양평군',31),(19,'의성군',35),(19,'함안군',36),(19,'장성군',38),(20,'영등포구',1),(20,'여주시',31),(20,'청도군',35),(20,'함양군',36),(20,'장흥군',38),(21,'용산구',1),(21,'연천군',31),(21,'청송군',35),(21,'합천군',36),(21,'진도군',38),(22,'은평구',1),(22,'오산시',31),(22,'칠곡군',35),(22,'함평군',38),(23,'종로구',1),(23,'용인시',31),(23,'포항시',35),(23,'해남군',38),(24,'중구',1),(24,'의왕시',31),(24,'화순군',38),(25,'중랑구',1),(25,'의정부시',31),(26,'이천시',31),(27,'파주시',31),(28,'평택시',31),(29,'포천시',31),(30,'하남시',31),(31,'화성시',31);
UNLOCK TABLES;

-- 테마 초기화 --
delete from theme;
INSERT INTO `theme` 
VALUES 
('A', '음식/미식 탐방'), 
('B', '쇼핑'), 
('C', '역사/문화유적/전통문화 체험'), 
('D', '자연 풍경 감상'), 
('E', 'K-Culture 체험'),
('F', '유흥/놀이 시설 체험'),
('G', '레저/스포츠'),
('H', '현대 문화 체험');

-- 카테고리 초기화 --
delete from category;
INSERT INTO `category` 
VALUES 
('A01', '한식', 'A'), 
('A02', '서양식', 'A'), 
('A03', '일식', 'A'), 
('A04', '중식', 'A'), 
('A05', '이색음식점', 'A'), 
('A06', '카페/전통찻집', 'A'), 
('A07', '클럽', 'A'),
('B01', '대형 서점', 'B'), 
('B02', '5일장', 'B'), 
('B03', '상설시장', 'B'), 
('B04', '백화점', 'B'), 
('B05', '면세점', 'B'), 
('B06', '대형마트', 'B'), 
('B07', '전문매장/상가', 'B'), 
('B08', '공예/공방', 'B'), 
('B09', '특산물판매점', 'B'), 
('B10', '사후면세점', 'B'),
('C01', '고궁', 'C'), 
('C02', '성', 'C'), 
('C03', '문', 'C'), 
('C04', '고택', 'C'), 
('C05', '생가', 'C'), 
('C06', '민속마을', 'C'), 
('C07', '유적지/사적지', 'C'), 
('C08', '사찰', 'C'), 
('C09', '종교성지', 'C'), 
('C10', '안보관광', 'C'), 
('C11', '농,산,어촌 체험', 'C'), 
('C12', '전통체험', 'C'), 
('C13', '산사체험', 'C'), 
('C14', '박물관', 'C'), 
('C15', '기념관', 'C'), 
('C16', '전시관', 'C'), 
('C17', '문화전수시설', 'C'), 
('C18', '어학당', 'C'), 
('C19', '문화관광축제', 'C'), 
('C20', '전통공연', 'C'), 
('C21', '한옥', 'C'),
('D01', '국립공원', 'D'), 
('D02', '도립공원', 'D'), 
('D03', '군립공원', 'D'), 
('D04', '산', 'D'), 
('D05', '자연생태관광지', 'D'), 
('D06', '자연휴양림', 'D'), 
('D07', '수목원', 'D'), 
('D08', '폭포', 'D'), 
('D09', '계곡', 'D'), 
('D10', '약수터', 'D'), 
('D11', '해안절경', 'D'), 
('D12', '해수욕장', 'D'), 
('D13', '섬', 'D'), 
('D14', '항구/포구', 'D'), 
('D15', '등대', 'D'), 
('D16', '호수', 'D'), 
('D17', '강', 'D'), 
('D18', '동굴', 'D'), 
('D19', '회귀동, 식물', 'D'), 
('D20', '기암괴석', 'D'), 
('D21', '헬스투어', 'D'),
('E01', '드라마','E'), 
('E02', '영화','E'), 
('E03', '예능','E'), 
('E04', '아티스트','E'),
('F01', '온천/욕장/스파', 'F'), 
('F02', '이색찜질방', 'F'), 
('F03', '테마공원', 'F'), 
('F04', '공원', 'F'), 
('F05', '유람선/잠수항관광', 'F'), 
('F06', '도서관', 'F'), 
('F07', '영화관', 'F'), 
('F08', '일반축제', 'F'),
('G01', '수상레포츠', 'G'), 
('G02', '항공레포츠', 'G'), 
('G03', '수련시설', 'G'), 
('G04', '경기장', 'G'), 
('G05', '인라인(실내 인라인 포함)', 'G'), 
('G06', '자전거하이킹', 'G'), 
('G07', '카트', 'G'), 
('G08', '골프', 'G'), 
('G09', '경마', 'G'), 
('G10', '경륜', 'G'), 
('G11', '카지노', 'G'), 
('G12', '승마', 'G'), 
('G13', '스키/스노보드', 'G'), 
('G14', '스케이트', 'G'), 
('G15', '썰매장', 'G'), 
('G16', '수렵장', 'G'), 
('G17', '사격장', 'G'), 
('G18', '야영장, 오토캠핑장', 'G'), 
('G19', '암벽등반', 'G'), 
('G20', '서바이벌게임', 'G'), 
('G21', 'ATV', 'G'), 
('G22', 'MTB', 'G'), 
('G23', '오프로드', 'G'), 
('G24', '번지점프', 'G'), 
('G25', '스키(보드) 렌탈샵', 'G'), 
('G26', '트래킹', 'G'), 
('G27', '윈드서핑/제트스키', 'G'), 
('G28', '카약/카누', 'G'), 
('G29', '요트', 'G'), 
('G30', '노쿨링/스킨스쿠버다이빙', 'G'), 
('G31', '민물낚시', 'G'), 
('G32', '바다낚시', 'G'), 
('G33', '수영', 'G'), 
('G34', '래프팅', 'G'), 
('G35', '스카이다이빙', 'G'), 
('G36', '초경량비행', 'G'), 
('G37', '행글라이딩', 'G'), 
('G38', '열기구', 'G'), 
('G39', '복합 레포츠', 'G'),
('H01', '이색체험', 'H'), 
('H02', '이색거리', 'H'), 
('H03', '발전소', 'H'), 
('H04', '식음료', 'H'), 
('H05', '산업관광지 기타', 'H'), 
('H06', '전자-반도체', 'H'), 
('H07', '자동차', 'H'), 
('H08', '다리대교', 'H'), 
('H09', '기념탑/기념비/전망대', 'H'), 
('H10', '분수', 'H'), 
('H11', '동상', 'H'), 
('H12', '터널', 'H'), 
('H13', '유명건물', 'H'), 
('H14', '컨벤션센터', 'H'), 
('H15', '미술관/화랑', 'H'), 
('H16', '공연장', 'H'), 
('H17', '문화원', 'H'), 
('H18', '외국문화원', 'H'), 
('H19', '연극', 'H'), 
('H20', '뮤지컬', 'H'), 
('H21', '오페라', 'H'), 
('H22', '전시회', 'H'), 
('H23', '박람회', 'H'), 
('H24', '무용', 'H'), 
('H25', '클래식음악회', 'H'), 
('H26', '대중콘서트', 'H'), 
('H27', '영화', 'H'), 
('H28', '기타행사', 'H'); 
-- ---------------------
