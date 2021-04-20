const express = require("express");

const app = express();
app.use(express.json());

const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

app.post("/user", async (req, res) => {
  try {
    const user = await prisma.user.create({
      data: {
        ...req.body,
      },
    });
    res.send(user);
  } catch (error) {
    res.send(error);
  }
});

app.get("/users", async (req, res) => {
  try {
    const users = await prisma.user.findMany();
    res.send(users);
  } catch (error) {
    res.send(error);
  }
});

app.get("/users/:id/articles", async (req, res) => {
  try {
    const articles = await prisma.article.findMany({
      where: {
        userId: req.params.id,
      },
    });
    res.send(articles);
  } catch (error) {
    res.send(error);
  }
});

app.get("/articles", async (req, res) => {
  try {
    const articles = await prisma.article.findMany();
    res.send(articles);
  } catch (error) {
    res.send(error);
  }
});

require("./scrap");

app.listen(3000, () => {
  console.log("application listening on port 3000");
});
