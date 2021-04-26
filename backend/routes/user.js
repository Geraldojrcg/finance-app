const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const auth = require("../middlewares/auth");

const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

router.post("/users/register", async (req, res) => {
  try {
    const { password } = req.body;
    const hash = await bcrypt.hash(password, 10);
    const user = await prisma.user.create({
      data: {
        ...req.body,
        password: hash,
      },
    });
    user.password = undefined;
    const token = jwt.sign(user, process.env.SECRET, {
      expiresIn: "365d",
    });
    res.send({
      token,
      user,
    });
  } catch (error) {
    console.log(error);
    res.send(error);
  }
});

router.post("/users/login", async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = await prisma.user.findFirst({
      where: {
        email,
      },
      include: {
        articles: true,
      },
    });
    if (!user) return res.status(401).send({ error: "Usuário não encontrado" });
    if (!(await bcrypt.compare(password, user.password))) {
      return res.status(400).send({ error: "Senha inválida" });
    }
    user.password = undefined;
    const token = jwt.sign(user, process.env.SECRET, {
      expiresIn: "365d",
    });
    res.send({
      token,
      user,
    });
  } catch (error) {
    console.log(error);
    res.send(error);
  }
});

router.get("/users", auth, async (req, res) => {
  try {
    const users = await prisma.user.findMany({
      include: {
        articles: true,
      },
    });
    res.send(users);
  } catch (error) {
    console.log(error);
    res.send(error);
  }
});

router.get("/users/:id", auth, async (req, res) => {
  try {
    const { id } = req.params;
    const users = await prisma.user.findUnique({
      where: {
        id: parseInt(id),
      },
      include: {
        articles: true,
      },
    });
    res.send(users);
  } catch (error) {
    console.log(error);
    res.send(error);
  }
});

router.get("/users/:id/articles", auth, async (req, res) => {
  try {
    const articles = await prisma.article.findMany();
    const readedArticles = await prisma.userArticles.findMany({
      where: {
        userId: parseInt(req.params.id),
      },
    });
    const userArticles = articles.map((a) => ({ ...a, readed: false }));
    readedArticles.forEach((article) => {
      let found = userArticles.findIndex((a) => a.id === article.articleId);
      if (found != -1) {
        userArticles[found].readed = true;
      }
    });
    userArticles.sort((a, b) => (a.createdAt < b.createdAt ? 1 : -1));
    res.send([
      ...userArticles.filter((a) => !a.readed),
      ...userArticles.filter((a) => a.readed),
    ]);
  } catch (error) {
    console.log(error);
    res.send(error);
  }
});

router.post(
  "/users/:user_id/articles/:article_id/read",
  auth,
  async (req, res) => {
    try {
      const { user_id, article_id } = req.params;
      const result = await prisma.$queryRaw`INSERT INTO UserArticles (userId, articleId, readed) VALUES (${user_id}, ${article_id}, true)`;
      res.send(result);
    } catch (error) {
      console.log(error);
      res.send(error);
    }
  }
);

module.exports = (app) => app.use("/api", router);
