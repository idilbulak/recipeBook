FROM postman/newman

WORKDIR /etc/newman

COPY ./recipeBookTests.postman_collection.json ./

CMD ["run", "./recipeBookTests.postman_collection.json"]