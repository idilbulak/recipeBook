name = recipeBook

all:
	@mkdir -p $(HOME)/data/
	@mkdir -p $(HOME)/data/recipeBook
	@docker-compose -f ./docker-compose.yml build

up:
	@docker-compose -f ./docker-compose.yml up

down:
	@docker-compose -f ./docker-compose.yml down

re:
	@docker-compose -f ./docker-compose.yml build

clean:
	@docker-compose -f ./docker-compose.yml down
	@docker system prune -af
	@docker volume rm $$(docker volume ls -q)
	@rm -rf $(HOME)/data/

.PHONY	: all up down re clean