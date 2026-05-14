#!/bin/bash

echo "Installing Java 21..."
sudo apt update
sudo apt install -y openjdk-21-jdk

echo "Installing Maven..."
sudo apt install -y maven

echo "Installing Node.js 20..."
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs

echo "Installing Angular CLI..."
npm install -g @angular/cli

echo "Installing Docker..."
sudo apt install -y docker.io
sudo usermod -aG docker $USER

echo "Installing Claude Code..."
npm install -g @anthropic-ai/claude-code

echo "Starting Postgres container..."
docker run --name invoice-postgres \
  -e POSTGRES_USER=invoiceuser \
  -e POSTGRES_PASSWORD=invoicepass \
  -e POSTGRES_DB=invoicedb \
  -p 5432:5432 \
  -d postgres:16

echo "All done! Run 'code .' to open in VSCode"