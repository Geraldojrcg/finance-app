const puppeteer = require("puppeteer");
const cron = require("node-cron");

const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

async function getArticleLinks(page) {
  return await page.$$eval("#infiniteScroll a", (anchors) => {
    let links = [];
    anchors.forEach((link) => {
      if (!links.some((l) => l == link.href)) {
        links.push(link.href);
      }
    });
    return links;
  });
}

async function extractAticleData(page) {
  let title = await page.$eval(".page-title-1", (el) => el.textContent);
  let images = await page.$$eval(".article-content figure img", (el) =>
    el.map((img) => img.src)
  );
  let paragraphs = await page.$$eval(".article-content p", (el) =>
    el
      .map((p) =>
        p.textContent.replace("<strong>", "").replace("</strong>", "")
      )
      .join("\n")
  );
  return {
    title,
    image: images[0] || "",
    body: paragraphs || "",
  };
}

async function makeScrap() {
  const browser = await puppeteer.launch({
    headless: true,
  });
  const page = await browser.newPage();
  await page.goto("https://www.infomoney.com.br/minhas-financas/");

  let articleLinks = await getArticleLinks(page);

  for (let link of articleLinks) {
    await page.goto(link);
    const article = await extractAticleData(page);

    const dbArticle = await prisma.article.findFirst({
      where: {
        title: article.title,
      },
    });

    if (!dbArticle) {
      await prisma.article.create({
        data: article,
      });
    }
  }

  await browser.close();
}

const job = cron.schedule("*/10 * * * *", async () => {
  console.log("Runing scrap every 10min");
  await makeScrap();
});
job.start();
