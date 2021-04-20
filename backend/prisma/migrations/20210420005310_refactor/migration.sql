-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Article" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "title" TEXT NOT NULL,
    "body" TEXT NOT NULL,
    "image" TEXT NOT NULL,
    "readed" BOOLEAN NOT NULL DEFAULT false,
    "userId" INTEGER,
    FOREIGN KEY ("userId") REFERENCES "User" ("id") ON DELETE SET NULL ON UPDATE CASCADE
);
INSERT INTO "new_Article" ("id", "createdAt", "title", "body", "image") SELECT "id", "createdAt", "title", "body", "image" FROM "Article";
DROP TABLE "Article";
ALTER TABLE "new_Article" RENAME TO "Article";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
