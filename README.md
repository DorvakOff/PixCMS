# PixCMS

> Application starts at [`http://localhost:8080`](http://localhost:8080)

# Development

> Run configurations for IntelliJ Idea are given in this repo ([.run/](./.run))

## Building for production
1. Run ``npm run build`` in the src/main/frontend directory
2. Run ``mvn clean package`` in the root directory

## Development
### Only backend
- Run ``npm start`` in the src/main/frontend directory

### Only frontend
- Run ``fr.pixteam.pixcms.Application`` under Java **11**

### Both
> Each time you need to do this, we recommend using custom IDE run configurations.
1. Run ``npm run build`` in the src/main/frontend directory
2. Run ``fr.pixteam.pixcms.Application`` under Java **11**
