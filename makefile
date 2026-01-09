.PHONY: run test build

run:
	clj -M:run

test:
	clj -X:test

build:
	docker build -t garden .