name = recipeBook

all:
	@docker-compose -f ./docker-compose.yml build

up:
	@docker-compose -f ./docker-compose.yml up

down:
	@docker-compose -f ./docker-compose.yml down

re:
	@docker-compose -f ./docker-compose.yml build
	@docker-compose -f ./docker-compose.yml up

clean:
	@docker-compose -f ./docker-compose.yml down
	@docker system prune -af

.PHONY	: all up down re clean