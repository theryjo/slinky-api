CREATE TABLE IF NOT EXISTS link_import (
    `link_import_id` INT PRIMARY KEY,
    `type` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `started_at` DATETIME NOT NULL,
    `finished_at` DATETIME,
    `completed` TINYINT NOT NULL DEFAULT 0,
    `count` INT
);

CREATE TABLE IF NOT EXISTS link (
    `link_id` INT PRIMARY KEY,
    `link_import_id` INT NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `value` VARCHAR(255) NOT NULL,
    FOREIGN KEY (link_import_id) REFERENCES link_import(link_import_id)
);
