#groc --glob "../../../app/**/*.java" --out "../../grocdoc"

groc --index ../../../README.md --glob "../../../*.md" --glob "../../../app/**/*.java" --glob "../../../public/angular/app/scripts/*.js" --out "../../../docs/grocdoc"
