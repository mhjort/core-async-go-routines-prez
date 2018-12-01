package main

import "fmt"

func main() {
	ping()
}

func ping() {
	c := make(chan string)
	go func() { c <- "ping" }()
	msg := <-c
	fmt.Println(msg)
}
