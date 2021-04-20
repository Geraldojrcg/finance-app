const express = require("express");
const router = express.Router();

const { PrismaClient } = require("@prisma/client");
const auth = require("../middlewares/auth");

const prisma = new PrismaClient();

router.use(auth);

router.get("/articles", async (req, res) => {
  try {
    const articles = await prisma.article.findMany();
    res.send(articles);
  } catch (error) {
    res.send(error);
  }
});

module.exports = (app) => app.use("/api", router);
