
create database firstTimeInKorea;
use firstTimeInKorea;


-- 시도 정보 --
CREATE TABLE `sido` (
  `sido_code` int NOT NULL,
  `sido_name` varchar(30) DEFAULT NULL,
  `sido_description` varchar(3000) NULL DEFAULT NULL,
  `sido_image` varchar(250) NULL DEFAULT NULL,
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
  `title` varchar(300) DEFAULT NULL,
  `addr1` varchar(300) DEFAULT NULL,
  `addr2` varchar(300) DEFAULT NULL,
  `zipcode` varchar(50) DEFAULT NULL,
  `tel` varchar(200) DEFAULT NULL,
  `first_image` varchar(250) DEFAULT NULL,
  `first_image2` varchar(250) DEFAULT NULL,
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
  `theme_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`theme_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 카테고리 분류 --
CREATE TABLE `category` (
  `category_code` varchar(5) NOT NULL,
  `category_name` varchar(100) DEFAULT NULL,
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
  `overview` varchar(10000) DEFAULT NULL,
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
    `join_date` TIMESTAMP NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 1,
    `token` VARCHAR(255) NULL,
	PRIMARY KEY (`id`)
)
ENGINE = InnoDB
DEFAULT CHARSET=utf8mb4;

-- 여행 계획 테이블
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

-- 여행 계획 및 관광지 관계 테이블
CREATE TABLE `plan_and_attraction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int DEFAULT NULL,
  `content_id` int DEFAULT NULL,
  `date` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `content_id` (`content_id`),
  CONSTRAINT `plan_and_attraction_to_plan_id_fk` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`) ON DELETE CASCADE,
  CONSTRAINT `plan_and_attraction_to_attraction_info_fk_id_fk` FOREIGN KEY (`content_id`) REFERENCES `attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 여행 계획 메모 테이블
CREATE TABLE plan_memo (
  plan_and_attraction_id int NOT NULL,
  text varchar(1000) DEFAULT NULL,
  modified_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (plan_and_attraction_id),
  CONSTRAINT plan_memo_to_plan_and_attraction_id_fk FOREIGN KEY (plan_and_attraction_id) REFERENCES plan_and_attraction (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 여행 계획 썸네일 테이블
CREATE TABLE `plan_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int NOT NULL,
  `save_folder` varchar(45) NOT NULL,
  `origin_file` varchar(50) NOT NULL,
  `save_file` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `plan_file_to_plan_plan_id_fk` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`) ON DELETE CASCADE
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
	`parent_article_comment_id` INT,
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
CREATE TABLE `bookmark` (
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

-- 한국 문화 관광지 정보 (기본) --
CREATE TABLE `k_attraction_info` (
  `content_id` int NOT NULL,
  `sido_code` int DEFAULT NULL,
  `gugun_code` int DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL,
  `addr1` varchar(300) DEFAULT NULL,
  `addr2` varchar(300) DEFAULT NULL,
  `zipcode` varchar(50) DEFAULT NULL,
  `tel` varchar(200) DEFAULT NULL,
  `first_image` varchar(250) DEFAULT NULL,
  `first_image2` varchar(250) DEFAULT NULL,
  `latitude` decimal(20,17) DEFAULT NULL,
  `longitude` decimal(20,17) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `k_attraction_to_sido_code_fk_idx` (`sido_code`),
  KEY `k_attraction_to_gugun_code_fk_idx` (`gugun_code`),
  CONSTRAINT `k_attraction_to_gugun_code_fk` FOREIGN KEY (`gugun_code`) REFERENCES `gugun` (`gugun_code`),
  CONSTRAINT `k_attraction_to_sido_code_fk` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`sido_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 한국 문화 관광지 상세 정보 (카테고리 분류) --
CREATE TABLE `k_attraction_detail` (
  `content_id` int NOT NULL,
  `theme_code` varchar(3) NOT NULL,
  `category_code` varchar(5) NOT NULL,
  `created_time` varchar(14) DEFAULT NULL,
  `modified_time` varchar(14) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  KEY `k_attraction_to_theme_code_fk_idx` (`theme_code`),
  KEY `k_attraction_to_category_code_fk_idx` (`category_code`),
  CONSTRAINT `k_attraction_to_theme_code_fk` FOREIGN KEY (`theme_code`) REFERENCES `theme` (`theme_code`),
  CONSTRAINT `k_attraction_to_category_code_fk` FOREIGN KEY (`category_code`) REFERENCES `category` (`category_code`),
  CONSTRAINT `k_attraction_detail_to_basic_content_id_fk` FOREIGN KEY (`content_id`) REFERENCES `k_attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 한국 문화 관광지 정의 및 설명 --
CREATE TABLE `k_attraction_description` (
  `content_id` int NOT NULL,
  `overview` varchar(10000) DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `k_attraction_detail_to_attraciton_id_fk` FOREIGN KEY (`content_id`) REFERENCES `k_attraction_info` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ---------------------
use firsttimeinkorea;

-- 시도 초기화 --
INSERT INTO `sido` (sido_code, sido_name)
VALUES
(1, 'Seoul'),
(2, 'Incheon'),
(3, 'Daejeon'),
(4, 'Daegu'),
(5, 'Gwangju'),
(6, 'Busan'),
(7, 'Ulsan'),
(8, 'Sejong'),
(31, 'Gyeonggi-do'),
(32, 'Gangwon-do'),
(33, 'Chungcheongbuk-do'),
(34, 'Chungcheongnam-do'),
(35, 'Gyeongsangbuk-do'),
(36, 'Gyeongsangnam-do'),
(37, 'Jeollabuk-do'),
(38, 'Jeollanam-do'),
(39, 'Jeju-do');

-- 구군 초기화 --
INSERT INTO `gugun` (gugun_code, gugun_name, sido_code)
VALUES
(1, 'Gangnam-gu', 1),
(2, 'Gangdong-gu', 1),
(3, 'Gangbuk-gu', 1),
(4, 'Gangseo-gu', 1),
(5, 'Gwanak-gu', 1),
(6, 'Gwangjin-gu', 1),
(7, 'Guro-gu', 1),
(8, 'Geumcheon-gu', 1),
(9, 'Nowon-gu', 1),
(10, 'Dobong-gu', 1),
(11, 'Dongdaemun-gu', 1),
(12, 'Dongjak-gu', 1),
(13, 'Mapo-gu', 1),
(14, 'Seodaemun-gu', 1),
(15, 'Seocho-gu', 1),
(16, 'Seongdong-gu', 1),
(17, 'Seongbuk-gu', 1),
(18, 'Songpa-gu', 1),
(19, 'Yangcheon-gu', 1),
(20, 'Yeongdeungpo-gu', 1),
(21, 'Yongsan-gu', 1),
(22, 'Eunpyeong-gu', 1),
(23, 'Jongno-gu', 1),
(24, 'Jung-gu', 1),
(25, 'Jungnang-gu', 1),
(1, 'Ganghwa-gun', 2),
(2, 'Gyeyang-gu', 2),
(3, 'Michuhol-gu', 2),
(4, 'Namdong-gu', 2),
(5, 'Dong-gu', 2),
(6, 'Bupyeong-gu', 2),
(7, 'Seo-gu', 2),
(8, 'Yeonsu-gu', 2),
(9, 'Ongjin-gun', 2),
(10, 'Jung-gu', 2),
(1, 'Daedeok-gu', 3),
(2, 'Dong-gu', 3),
(3, 'Seo-gu', 3),
(4, 'Yuseong-gu', 3),
(5, 'Jung-gu', 3),
(1, 'Nam-gu', 4),
(2, 'Dalseo-gu', 4),
(3, 'Dalseong-gun', 4),
(4, 'Dong-gu', 4),
(5, 'Buk-gu', 4),
(6, 'Seo-gu', 4),
(7, 'Suseong-gu', 4),
(8, 'Jung-gu', 4),
(9, 'Gunwi-gun', 4),
(1, 'Gwangsan-gu', 5),
(2, 'Nam-gu', 5),
(3, 'Dong-gu', 5),
(4, 'Buk-gu', 5),
(5, 'Seo-gu', 5),
(1, 'Gangseo-gu', 6),
(2, 'Geumjeong-gu', 6),
(3, 'Gijang-gun', 6),
(4, 'Nam-gu', 6),
(5, 'Dong-gu', 6),
(6, 'Dongnae-gu', 6),
(7, 'Busanjin-gu', 6),
(8, 'Buk-gu', 6),
(9, 'Sasang-gu', 6),
(10, 'Saha-gu', 6),
(11, 'Seo-gu', 6),
(12, 'Suyeong-gu', 6),
(13, 'Yeonje-gu', 6),
(14, 'Yeongdo-gu', 6),
(15, 'Jung-gu', 6),
(16, 'Haeundae-gu', 6),
(1, 'Jung-gu', 7),
(2, 'Nam-gu', 7),
(3, 'Dong-gu', 7),
(4, 'Buk-gu', 7),
(5, 'Ulju-gun', 7),
(1, 'Sejong', 8),
(1, 'Gapyeong-gun', 31),
(2, 'Goyang-si', 31),
(3, 'Gwacheon-si', 31),
(4, 'Gwangmyeong-si', 31),
(5, 'Gwangju-si', 31),
(6, 'Guri-si', 31),
(7, 'Gunpo-si', 31),
(8, 'Gimpo-si', 31),
(9, 'Namyangju-si', 31),
(10, 'Dongducheon-si', 31),
(11, 'Bucheon-si', 31),
(12, 'Seongnam-si', 31),
(13, 'Suwon-si', 31),
(14, 'Siheung-si', 31),
(15, 'Ansan-si', 31),
(16, 'Anseong-si', 31),
(17, 'Anyang-si', 31),
(18, 'Yangju-si', 31),
(19, 'Yangpyeong-gun', 31),
(20, 'Yeoju-si', 31),
(21, 'Yeoncheon-gun', 31),
(22, 'Osan-si', 31),
(23, 'Yongin-si', 31),
(24, 'Uiwang-si', 31),
(25, 'Uijeongbu-si', 31),
(26, 'Icheon-si', 31),
(27, 'Paju-si', 31),
(28, 'Pyeongtaek-si', 31),
(29, 'Pocheon-si', 31),
(30, 'Hanam-si', 31),
(31, 'Hwaseong-si', 31),
(1, 'Gangneung-si', 32),
(2, 'Goseong-gun', 32),
(3, 'Donghae-si', 32),
(4, 'Samcheok-si', 32),
(5, 'Sokcho-si', 32),
(6, 'Yanggu-gun', 32),
(7, 'Yangyang-gun', 32),
(8, 'Yeongwol-gun', 32),
(9, 'Wonju-si', 32),
(10, 'Inje-gun', 32),
(11, 'Jeongseon-gun', 32),
(12, 'Cheorwon-gun', 32),
(13, 'Chuncheon-si', 32),
(14, 'Taebaek-si', 32),
(15, 'Pyeongchang-gun', 32),
(16, 'Hongcheon-gun', 32),
(17, 'Hwacheon-gun', 32),
(18, 'Hoengseong-gun', 32),
(1, 'Goesan-gun', 33),
(2, 'Danyang-gun', 33),
(3, 'Boeun-gun', 33),
(4, 'Yeongdong-gun', 33),
(5, 'Okcheon-gun', 33),
(6, 'Eumseong-gun', 33),
(7, 'Jecheon-si', 33),
(8, 'Jincheon-gun', 33),
(9, 'Cheongju-si', 33),
(10, 'Chungju-si', 33),
(11, 'Jeungpyeong-gun', 33),
(1, 'Gongju-si', 34),
(2, 'Geumsan-gun', 34),
(3, 'Nonsan-si', 34),
(4, 'Dangjin-si', 34),
(5, 'Boryeong-si', 34),
(6, 'Buyeo-gun', 34),
(7, 'Seosan-si', 34),
(8, 'Seocheon-gun', 34),
(9, 'Asan-si', 34),
(10, 'Yesan-gun', 34),
(11, 'Cheonan-si', 34),
(12, 'Cheongyang-gun', 34),
(13, 'Taean-gun', 34),
(14, 'Hongseong-gun', 34),
(1, 'Gyeongsan-si', 35),
(2, 'Gyeongju-si', 35),
(3, 'Goryeong-gun', 35),
(4, 'Gumi-si', 35),
(5, 'Gimcheon-si', 35),
(6, 'Mungyeong-si', 35),
(7, 'Bonghwa-gun', 35),
(8, 'Sangju-si', 35),
(9, 'Seongju-gun', 35),
(10, 'Andong-si', 35),
(11, 'Yeongdeok-gun', 35),
(12, 'Yeongyang-gun', 35),
(13, 'Yeongju-si', 35),
(14, 'Yeongcheon-si', 35),
(15, 'Yecheon-gun', 35),
(16, 'Ulleung-gun', 35),
(17, 'Uljin-gun', 35),
(18, 'Uiseong-gun', 35),
(19, 'Cheongdo-gun', 35),
(20, 'Cheongsong-gun', 35),
(21, 'Chilgok-gun', 35),
(22, 'Pohang-si', 35),
(1, 'Geoje-si', 36),
(2, 'Geochang-gun', 36),
(3, 'Goseong-gun', 36),
(4, 'Gimhae-si', 36),
(5, 'Namhae-gun', 36),
(6, 'Miryang-si', 36),
(7, 'Sacheon-si', 36),
(8, 'Sancheong-gun', 36),
(9, 'Yangsan-si', 36),
(10, 'Uiryeong-gun', 36),
(11, 'Jinju-si', 36),
(12, 'Changnyeong-gun', 36),
(13, 'Changwon-si', 36),
(14, 'Tongyeong-si', 36),
(15, 'Hadong-gun', 36),
(16, 'Haman-gun', 36),
(17, 'Hamyang-gun', 36),
(18, 'Hapcheon-gun', 36),
(1, 'Gochang-gun', 37),
(2, 'Gunsan-si', 37),
(3, 'Gimje-si', 37),
(4, 'Namwon-si', 37),
(5, 'Muju-gun', 37),
(6, 'Buan-gun', 37),
(7, 'Sunchang-gun', 37),
(8, 'Wanju-gun', 37),
(9, 'Iksan-si', 37),
(10, 'Imsil-gun', 37),
(11, 'Jangsu-gun', 37),
(12, 'Jeonju-si', 37),
(13, 'Jeongeup-si', 37),
(14, 'Jinan-gun', 37),
(1, 'Gangjin-gun', 38),
(2, 'Goheung-gun', 38),
(3, 'Gokseong-gun', 38),
(4, 'Gwangyang-si', 38),
(5, 'Gurye-gun', 38),
(6, 'Naju-si', 38),
(7, 'Damyang-gun', 38),
(8, 'Mokpo-si', 38),
(9, 'Muan-gun', 38),
(10, 'Boseong-gun', 38),
(11, 'Suncheon-si', 38),
(12, 'Sinan-gun', 38),
(13, 'Yeosu-si', 38),
(14, 'Yeonggwang-gun', 38),
(15, 'Yeongam-gun', 38),
(16, 'Wando-gun', 38),
(17, 'Jangseong-gun', 38),
(18, 'Jangheung-gun', 38),
(19, 'Jindo-gun', 38),
(20, 'Hampyeong-gun', 38),
(21, 'Haenam-gun', 38),
(22, 'Hwasun-gun', 38),
(3, 'Seogwipo-si', 39),
(4, 'Jeju-si', 39);

-- 테마 초기화 --
delete from theme;
INSERT INTO `theme` (theme_code, theme_name)
VALUES 
('A', 'Food/Culinary Exploration'),
('B', 'Shopping'),
('C', 'History/Cultural Heritage/Traditional Culture Experience'),
('D', 'Enjoying Natural Landscapes'),
('E', 'K-Culture Experience'),
('F', 'Entertainment/Amusement Facilities Experience'),
('G', 'Leisure/Sports'),
('H', 'Modern Culture Experience');

-- 카테고리 초기화 --
delete from category;
INSERT INTO `category` (category_code, category_name, theme_code)
VALUES 
('A01', 'Korean Restaurant', 'A'),
('A02', 'Western Restaurant', 'A'),
('A03', 'Japanese Restaurant', 'A'),
('A04', 'Chinese Restaurant', 'A'),
('A05', 'Unique Restaurant', 'A'),
('A06', 'Bar Cafe', 'A'),
('A07', 'Club', 'A'),
('B01', 'Bookstore', 'B'),
('B02', 'Day Market', 'B'),
('B03', 'Traditional Market', 'B'),
('B04', 'Department Store', 'B'),
('B05', 'Duty Free Shop', 'B'),
('B06', 'Discount Shop', 'B'),
('B07', 'Shop Boutique Outlet Mall', 'B'),
('B08', 'Craft Workshop', 'B'),
('B09', 'Specialty Shop', 'B'),
('B10', 'Tax Refund Shop', 'B'),
('C01', 'Palace', 'C'),
('C02', 'Fortress', 'C'),
('C03', 'Gate', 'C'),
('C04', 'Old House', 'C'),
('C05', 'Birthplace', 'C'),
('C06', 'Folk Village', 'C'),
('C07', 'Historic Site', 'C'),
('C08', 'Temple', 'C'),
('C09', 'Holy Site', 'C'),
('C10', 'Security Peace Site', 'C'),
('C11', 'Village Experience', 'C'),
('C12', 'Traditional Experience', 'C'),
('C13', 'Temple Stay', 'C'),
('C14', 'Museum', 'C'),
('C15', 'Memorial Hall', 'C'),
('C16', 'Exhibition Hall', 'C'),
('C17', 'Cultural Training Center', 'C'),
('C18', 'Language Institute', 'C'),
('C19', 'Special Festival', 'C'),
('C20', 'Traditional Performance', 'C'),
('C21', 'Hanok Stay', 'C'),
('D01', 'National Park', 'D'),
('D02', 'Provincial Park', 'D'),
('D03', 'County Park', 'D'),
('D04', 'Mountain', 'D'),
('D05', 'Eco Tourism Site', 'D'),
('D06', 'Recreational Forest', 'D'),
('D07', 'Botanical Garden', 'D'),
('D08', 'Waterfall', 'D'),
('D09', 'Valley', 'D'),
('D10', 'Mineral Spring', 'D'),
('D11', 'Coastal Attraction', 'D'),
('D12', 'Beach', 'D'),
('D13', 'Island', 'D'),
('D14', 'Port Harbor', 'D'),
('D15', 'Lighthouse', 'D'),
('D16', 'Lake', 'D'),
('D17', 'River', 'D'),
('D18', 'Cave', 'D'),
('D19', 'Rare Animal Plant', 'D'),
('D20', 'Rock', 'D'),
('D21', 'Health Tour', 'D'),
('E01', 'Drama', 'E'),
('E02', 'Movie', 'E'),
('E03', 'Entertainment', 'E'),
('E04', 'Artist', 'E'),
('F01', 'Hot Spring Spa', 'F'),
('F02', 'Dry Sauna', 'F'),
('F03', 'Theme Park', 'F'),
('F04', 'Park', 'F'),
('F05', 'Cruise Submarine', 'F'),
('F06', 'Library', 'F'),
('F07', 'Movie Theater', 'F'),
('F08', 'Regular Festival', 'F'),
('G01', 'Water Leisure Sport', 'G'),
('G02', 'Sky Leisure Sport', 'G'),
('G03', 'Training Facility', 'G'),
('G04', 'Stadium', 'G'),
('G05', 'Inline Skate', 'G'),
('G06', 'Bicycle Hike', 'G'),
('G07', 'Go Kart', 'G'),
('G08', 'Golf', 'G'),
('G09', 'Horse Race', 'G'),
('G10', 'Bicycle Race', 'G'),
('G11', 'Casino', 'G'),
('G12', 'Horse Ride', 'G'),
('G13', 'Skiing Snowboarding', 'G'),
('G14', 'Ice Skate', 'G'),
('G15', 'Sledding', 'G'),
('G16', 'Hunting Ground', 'G'),
('G17', 'Shooting Range', 'G'),
('G18', 'Camp', 'G'),
('G19', 'Rock Climb', 'G'),
('G20', 'Survival Game', 'G'),
('G21', 'All Terrain Vehicle', 'G'),
('G22', 'Mountain Bike', 'G'),
('G23', 'Off Road Vehicle', 'G'),
('G24', 'Bungee Jump', 'G'),
('G25', 'Ski Snowboard Rental', 'G'),
('G26', 'Trek', 'G'),
('G27', 'Wind Surfing Jet Ski', 'G'),
('G28', 'Kayak Canoe', 'G'),
('G29', 'Yacht', 'G'),
('G30', 'Snorkeling Scuba Diving', 'G'),
('G31', 'Freshwater Fishing', 'G'),
('G32', 'Sea Fishing', 'G'),
('G33', 'Swim', 'G'),
('G34', 'Raft', 'G'),
('G35', 'Sky Dive', 'G'),
('G36', 'Ultralight Flight', 'G'),
('G37', 'Hang Gliding Paragliding', 'G'),
('G38', 'Hot Air Balloon', 'G'),
('G39', 'Other Leisure Sport', 'G'),
('H01', 'Unique Experience', 'H'),
('H02', 'Cultural District', 'H'),
('H03', 'Power Plant', 'H'),
('H04', 'Food Beverage', 'H'),
('H05', 'Industrial Tourist', 'H'),
('H06', 'Electronic Semiconductor', 'H'),
('H07', 'Automobile', 'H'),
('H08', 'Bridge', 'H'),
('H09', 'Monument Observatory', 'H'),
('H10', 'Fountain', 'H'),
('H11', 'Statue', 'H'),
('H12', 'Tunnel', 'H'),
('H13', 'Building', 'H'),
('H14', 'Convention Center', 'H'),
('H15', 'Art Museum Gallery', 'H'),
('H16', 'Performance Hall', 'H'),
('H17', 'Korean Cultural Center', 'H'),
('H18', 'Foreign Cultural Center', 'H'),
('H19', 'Play', 'H'),
('H20', 'Musical', 'H'),
('H21', 'Opera', 'H'),
('H22', 'Exhibition', 'H'),
('H23', 'Exposition', 'H'),
('H24', 'Dance', 'H'),
('H25', 'Classical Music Concert', 'H'),
('H26', 'Popular Music Concert', 'H'),
('H27', 'Movie', 'H'),
('H28', 'Other Event', 'H'); 
-- ---------------------

-- 시도정보 초기화 --
update sido set sido_description='Seoul, city and capital of South Korea (the Republic of Korea). It is located on the Han River (Han-gang) in the northwestern part of the country, with the city center some 37 miles (60 km) inland from the Yellow Sea (west). Seoul is the cultural, economic, and political center of South Korea.

Except for a brief interregnum (1399–1405), Seoul was the capital of Korea from 1394 until the formal division of the country in 1948. The name itself has come to mean “capital” in the Korean language. The city was popularly called Seoul in Korean during both the Joseon (Yi) dynasty (1392–1910) and the period of Japanese rule (1910–45), although the official names in those periods were Hanseong and Gyeongseong, respectively. The city was also popularly and, during most of the 14th century, officially known as Hanyang. Seoul became the official name of the city only with the founding of South Korea in 1948. Area 234 square miles (605 square km). Pop. (2020) 9,586,195.'
,sido_image = 'https://cdn.britannica.com/57/75757-050-122EC2ED/Changgyong-Palace-background-Seoul.jpg'
where sido_name= 'Seoul';

update sido set sido_description='Incheon, port city, Gyeonggi do (province), northwestern South Korea. It lies near the mouth of the Han River, 25 miles (40 km) west-southwest of Seoul, with which it is connected by highway and railroad. It serves as the capital’s chief seaport and is the site of South Korea’s main international airport. Incheon has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province.

A fishing port since the Joseon (Yi) dynasty (1392–1910), Incheon became one of three Korean treaty ports in 1883 and developed as an international commercial port before the Japanese occupation (1910–45). During the occupation the city was renamed Jinsen; industries and port facilities were further developed, and tidal basins were constructed to overcome the 33-foot (10-meter) difference between low and high tides. During the Korean War (1950–53), a successful United Nations troop landing at Incheon in mid-September 1950 crippled the North Korean invasion, and, to commemorate it, a huge statue of U.S. Gen. Douglas MacArthur was erected in Jayu Park, overlooking the port.'
,sido_image = 'https://goguides.azureedge.net/media/guxoaooq/f2350b07-6004-4311-899c-e0c01de88a8c.jpg?anchor=center&mode=crop&width=1600&height=1066&quality=50'
where sido_name= 'Incheon';

update sido set sido_description='Daejeon, metropolitan city, west-central South Korea. Daejeon has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province.

Until the end of the Joseon (Yi) dynasty (1392–1910) Daejeon was a poor village located on the Daejeon River. The modern city has grown to encompass the area around the Gap and Yudeung rivers as well. Development began after the juncture of rail lines in 1905 and 1914. It is connected with Seoul (about 100 miles [160 km] to the north-northeast), Busan, and Mokpo by highway and by regular and high-speed rail lines. During the Korean War (1950–53) it was a temporary capital of the Republic of Korea. About 70 percent of the city was destroyed in the war, but it was rebuilt in the postwar years. The central location of Daejeon, coupled with a national policy designed to balance the concentration of population and activity in Seoul, led to the construction of a Central Administrative Complex in the Dunsan district of the city. The establishment of the complex also reflected the need for a more effective administrative system.'
,sido_image = 'https://res.heraldm.com/content/image/2015/06/05/20150605001415_0.jpg'
where sido_name= 'Daejeon';

update sido set sido_description='Daegu, metropolitan city, southeastern South Korea. Daegu is one of South Korea’s largest urban areas and has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province. It lies east of the confluence of the Nakdong and Geumho rivers and 55 miles (90 km) north-northwest of Busan.

For centuries Daegu was the administrative, economic, and cultural center of southeastern Korea. During the Joseon (Yi) dynasty (1392–1910) it was the capital of the province of Gyeongsang (until the province was divided into North and South Gyeongsang in 1896) and one of the country’s three big market cities. Daegu underwent explosive growth from the 1950s and increased 10-fold in population in the decades after the Korean War (1950–53).
'
,sido_image = 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzExMTJfMTE4%2FMDAxNjk5Nzk1NTA5Mjg0.LqfVMZX3EM5D1Tr9o_gN7rKhi6fyko7If8WkRQdNuEgg.bIFQAr9lgEBlvcwJhieQA6sE0QUKO0C5CFfMBa3-fpcg.JPEG.bluetime01%2F3X9A9088.jpg&type=sc960_832'
where sido_name= 'Daegu';

update sido set sido_description='Gwangju, metropolitan city, southwestern South Korea. It has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province. An old city bordering the mountainous area of South Jeolla province, Gwangju is located at the foot of Mount Mudeung, which rises to 3,894 feet (1,187 meters).

The city has been a center of trade and of local administration since the Three Kingdoms period (c. 57 BCE–668 CE). Modern industries, including cotton textiles, breweries, and rice mills, began with the building of a railway from Seoul in 1914. During the Korean War (1950–53) Gwangju’s suburbs became a major military training center. From 1967, with the construction of an industrial zone centering on an automobile factory, the city grew rapidly. Developments included storage and processing facilities for agricultural products. Gwangju was the site of an armed uprising against the newly installed military government of Chun Doo-Hwan in May 1980. More than 140 civilians were killed during the suppression of the protest; it is now commemorated with an annual festival held on May 18.'
,sido_image = 'https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyNDAzMTlfMTAw%2FMDAxNzEwODQ3MjgxNzgx.Hzdv3jcQfd_3Vvn-YZSwG4beA1fxizyACofK1g2d7v8g.64cdjBBLWy0D3jWeA6P3u8H3fb87LlAAWyxp1GoM9HUg.JPEG%2FDSC02484-Enhanced-NR.jpg&type=sc960_832'
where sido_name= 'Gwangju';

update sido set sido_description='Busan, metropolitan city and port, South Korea, located at the southeast tip of the Korean peninsula. It is bordered to the north and west by South Gyeongsang province (do); to the south and east lies the Korea Strait. During the Goryeo dynasty (935–1392) it was named Pusanp’o (Korean pu meaning “kettle” and san meaning “mountain,” for the shape of the mountain at whose foot it is situated, and p’o meaning “bay” or “harbor”). Busan is the country’s largest port and second largest city. It has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province.

On a deep well-sheltered bay at the mouth of the Naktong River, facing the Japanese islands of Tsushima across the Korea Strait, Busan was opened to the Japanese in 1876 and to general foreign trade in 1883. Under the Japanese occupation (1910–45) it developed into a modern port; ferry service connected the city with Shimonoseki, Japan, and Busan was the terminus of rail lines connecting Korea to China and Russia. The city became overpopulated with repatriates from overseas when Korea gained independence in 1945 and again with refugees during the Korean War (1950–53), when it was the temporary capital of the Republic of Korea.'
,sido_image = 'https://i.namu.wiki/i/uS37PZN_Dz11o6w2-_TM-rxg9NEbI1RY6Qio5DYWL0dEb-CWdiDUiuU4lPL0LF5ATUM13kJTISSFwyL4aCOXLg.webp'
where sido_name= 'Busan';

update sido set sido_description='Ulsan, metropolitan city, southeastern South Korea. Ulsan has the status of a metropolitan city under the direct control of the central government, with administrative status equal to that of a province. At the eastern end of the Taebaek Mountains, facing the East Sea (Sea of Japan), on Ulsan Bay, it lies about 45 miles (72 km) north-northeast of Busan. It is the heart of the country’s special industrial area known as the Ulsan Industrial District.

Until 1962, when the city was connected by rail and highway with Seoul, Busan, Daegu, and Daejeon, it was primarily a fishing port and a market center for agricultural products (especially pears) from the Ulsan plain and the delta of the Taehwa River. By the end of the first five-year economic plan (1966), the city had become an open port with major manufacturing plants, and by the late 20th century it was one of the country’s most significant industrial hubs. A free-trade zone was established in the early 21st century. Among the city’s major industries are automobile manufacturing, petrochemicals, and shipbuilding. An airport located on the outskirts of Ulsan provides domestic service. Munsu Stadium was the venue for some of the 2002 football (soccer) World Cup championship matches.r'
,sido_image = 'https://facts.net/wp-content/uploads/2023/07/32-facts-about-ulsan-1688545372.jpeg'
where sido_name= 'Ulsan';

update sido set sido_description='Sejong City, city and planned capital, west-central South Korea. Relocation of the country’s capital was proposed in 2002 in order to reduce congestion in the current capital of Seoul. Sejong City was founded in 2007, combining areas of South Chungcheong and North Chungcheong into a long, narrow strip of land. Key government agencies began relocating there in 2012.

Sejong City was named after King Sejong the Great, the fourth ruler of the Joseon dynasty. He is celebrated for his reforms to Korean society, for overseeing a period of cultural flourishing, and most notably, for the invention of Hangul, the phonetic system for writing the Korean language that is still in use.'
,sido_image = 'https://marketplace.intelligentcitieschallenge.eu/files/imager/images/city/sejong/22920/sejong_city.JPG_28f048ec435706e3593a4d04ed813319.jpg'
where sido_name= 'Sejong';

update sido set sido_description='Gyeonggi, do (province), northwestern South Korea. It is bounded by the truce line (demilitarized zone) with North Korea (north), by the provinces of Gangwon (east) and North Gyeongsang and South Chungcheong (south), and by the Yellow Sea (west).

Formerly, Gyeonggi province was the granary of Seoul; the Gyeonggi plain, with the Han River and its tributaries flowing through it, produced rice, barley, and wheat. Dairying and truck farming and other types of horticulture are still carried on. As Seoul’s industrial district spread into the province’s area, and with the construction of highways beginning in the late 1960s, a large part of the province became the outer industrial region of Seoul. The cities of Anyang, Bucheon, Seongnam, and Uijeongbu have developed as satellites of Seoul, each carrying on various types of industries, such as shipbuilding, iron and steel manufacturing, and plate-glass production. The city of Incheon serves as Seoul’s seaport and includes the capital’s international airport. Suwon contains a number of notable historic and prehistoric structures. The province has several UNESCO World Heritage sites: Hwaseong Fortress (designated 1997); prehistoric dolmens (stone tombs) near Incheon (2000, collectively with dolmens in North Jeolla and South Jeolla provinces); and royal tombs of the Joseon dynasty (1392–1910) located around the province (2009, along with other such tombs located in Gangwon province and in Seoul). The sea around the Baengnyeong and Yeonpyeong island groups in the Gyeonggi Gulf offer good fishing grounds for yellow corbinas and croakers. The islands’ proximity to the northern border has made the gulf the site of occasional military incidents, such as North Korea’s November 2010 artillery attack on Yeonpyeong Island.'
,sido_image = 'https://cdn.imweb.me/thumbnail/20230920/4ff21c1655f07.jpg'
where sido_name= 'Gyeonggi-do';

update sido set sido_description='Gangwon, do (province), northeastern South Korea. It is bounded to the east by the East Sea (Sea of Japan), to the south by North Gyeongsang and North Chungcheong provinces, to the west by Gyeonggi province, and to the north by Gangwon province, North Korea. Prior to the division of the Korean peninsula after World War II, the two Gangwon provinces were a single entity, but that larger province was split between North and South Korea, first at latitude 38° N (the 38th parallel) in 1945 and then, following the Korean War armistice (1953), by the truce line (demilitarized zone); most of the original province is now in South Korea.

The Taebaek Mountains nearly reach the sea, and the contour of the coastline is steep and smooth. Of the province’s total area, four-fifths is woodland that contains forest products such as edible alpine plants and mushrooms. Mineral resources include iron, coal, tungsten, fluorite, and limestone. Other industries have been created by the development of hydroelectric and thermoelectric power plants. The nearby waters abound in fish, especially cuttlefish and pollack. Cities in the province are Chuncheon, the provincial capital; Gangneung; Wonju; and Sokcho. Both Mount Seorak (5,604 feet [1,708 meters]), with a ski run, and Mount Odae (5,128 feet [1,563 meters]) are in national parks in the Taebaek range. In 2009 a cluster of royal tombs of the Joseon dynasty (1392–1910) in southern Gangwon were designated a UNESCO World Heritage site along with other such tombs located in Gyeonggi province and in Seoul. Pyeongchang, in the south-central region of the province, was the site of the 2018 Winter Olympic Games.'
,sido_image = 'https://res.klook.com/image/upload/Mobile/City/xg0vz4w95pq9a90kvgxe.jpg'
where sido_name= 'Gangwon-do';
-- ------------------------------------
