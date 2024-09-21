package main

import (
	"archive/zip"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"time"
)

// File represents a file to be saved
type File struct {
	Name    string `json:"file"`
	Content string `json:"content"`
}

func main() {
	// Create data directory if it doesn't exist
	if _, err := os.Stat("./data"); os.IsNotExist(err) {
		os.Mkdir("./data", 0755)
	}

	// Create out directory if it doesn't exist
	if _, err := os.Stat("./out"); os.IsNotExist(err) {
		os.Mkdir("./out", 0755)
	}

	// Start a goroutine to zip the data directory every minute
	go func() {
		for {
			zipData()
			time.Sleep(1 * time.Minute)
		}
	}()

	go func() {
		files, err := os.ReadDir("./data")
		if err != nil {
			return
		}

		var fileNames string
		for _, file := range files {
			fileNames += file.Name() + "<br />"
		}

		os.WriteFile("out/list.html", []byte(fileNames), os.ModePerm)

		time.Sleep(10 * time.Second)
	}()

	http.HandleFunc("/", handleRoot)
	http.HandleFunc("/download", handleDownload)

	log.Fatal(http.ListenAndServe(":3000", nil))
}

func handleRoot(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		fmt.Println("serving /")

		http.ServeFile(w, r, "./out/list.html")
	}

	if r.Method == "POST" {
		var file File
		err := json.NewDecoder(r.Body).Decode(&file)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		fmt.Println("saving ", file)

		filePath := filepath.Join("./data", file.Name)
		err = os.WriteFile(filePath, []byte(file.Content), 0644)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusCreated)
	}

}

func handleDownload(w http.ResponseWriter, r *http.Request) {
	// if r.Method != "GET" {
	// 	http.Error(w, "Invalid request method", http.StatusBadRequest)
	// 	return
	// }

	// fmt.Println("serving dump")

	// w.Header().Set("Content-Disposition", "attachment; filename=\"alldata.zip\"")
	// w.Header().Set("Content-Type", "application/zip")

	// http.ServeFile(w, r, "./out/alldata.zip")

	file, err := os.Open("./out/alldata.zip")
	if err != nil {
		http.Error(w, "File not found", http.StatusNotFound)
		return
	}
	defer file.Close()

	fileInfo, err := file.Stat()
	if err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	http.ServeContent(w, r, fileInfo.Name(), fileInfo.ModTime(), file)
}

func zipData() {
	fmt.Println(time.UnixDate, " zipping")

	// Remove existing zip file
	err := os.Remove("./out/alldata.zip")
	if err != nil && !os.IsNotExist(err) {
		log.Println(err)
		return
	}

	// Create a new zip file
	zipFile, err := os.Create("./out/alldata.zip")
	if err != nil {
		log.Println(err)
		return
	}
	defer zipFile.Close()

	// Create a zip writer
	zipWriter := zip.NewWriter(zipFile)
	defer zipWriter.Close()

	// Walk the data directory and add files to the zip
	err = filepath.Walk("./data", func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}

		if !info.IsDir() {
			file, err := os.Open(path)
			if err != nil {
				return err
			}
			defer file.Close()

			// Get the relative path to the file
			relPath, err := filepath.Rel("./data", path)
			if err != nil {
				return err
			}

			// Add the file to the zip
			fileWriter, err := zipWriter.Create(relPath)
			if err != nil {
				return err
			}

			_, err = io.Copy(fileWriter, file)
			if err != nil {
				return err
			}
		}

		return nil
	})

	if err != nil {
		log.Println(err)
	}
}
