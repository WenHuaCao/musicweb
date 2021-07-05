/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50649
 Source Host           : localhost:3306
 Source Schema         : music

 Target Server Type    : MySQL
 Target Server Version : 50649
 File Encoding         : 65001

 Date: 05/07/2021 10:54:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `create_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int(32) NOT NULL,
  `song_id` int(32) UNSIGNED NOT NULL,
  `body` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `song_id`(`song_id`) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for playlist
-- ----------------------------
DROP TABLE IF EXISTS `playlist`;
CREATE TABLE `playlist`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `builddate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int(32) UNSIGNED NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for playlist_song
-- ----------------------------
DROP TABLE IF EXISTS `playlist_song`;
CREATE TABLE `playlist_song`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `song_id` int(32) UNSIGNED NOT NULL,
  `playlist_id` int(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `playlist_song_ibfk_1`(`song_id`) USING BTREE,
  INDEX `playlist_song_ibfk_2`(`playlist_id`) USING BTREE,
  CONSTRAINT `playlist_song_ibfk_1` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `playlist_song_ibfk_2` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song`  (
  `id` int(32) UNSIGNED NOT NULL AUTO_INCREMENT,
  `duration_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `singer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `album_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `song_words` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `is_valid` int(32) NOT NULL,
  `user_id` int(32) UNSIGNED NOT NULL,
  `songname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `song_file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `PASSWORD` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` int(32) NOT NULL DEFAULT 0,
  `file_num` int(32) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_playlist
-- ----------------------------
DROP TABLE IF EXISTS `user_playlist`;
CREATE TABLE `user_playlist`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` int(32) NOT NULL,
  `playlist_id` int(32) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_playlist_ibfk_1`(`user_id`) USING BTREE,
  INDEX `user_playlist_ibfk_2`(`playlist_id`) USING BTREE,
  CONSTRAINT `user_playlist_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_playlist_ibfk_2` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
