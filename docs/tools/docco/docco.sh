docco -o . -l linear ../../../public/angular/app/scripts/app.js
wkhtmltopdf app.html ../../nu-planner-frontend.pdf
rm app.html
rm docco.css
rm -r public

cp ../../nu-planner-frontend.pdf ../../../public/nu-planner-frontend.pdf