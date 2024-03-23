# Flashcards-Client
#### Vue.JS 3 frontend for the Flashcards app using Vuetify 3


## Folder structure of frontend module

| Folder/File                                                      | Description                                       |
|------------------------------------------------------------------|---------------------------------------------------|
| `frontend`                                                       | Root folder of the frontend module                |
| &nbsp;&nbsp;&nbsp;&nbsp;`src`                                    | Contains all source files                         |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`assets`         | Contains static assets like images, fonts etc.    |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`feature`        | Pages, components, composables of features        |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`layouts`        | Layouts templates                                 |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`plugins`        | Contains setup of plugins (vue-router, vuetify)   |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`router`         | Contains Vue router configuration and routes      |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`shared`         | Feature-shared pages, components, composables     |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`App.vue`        | Main Vue component                                |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`main.ts`        | Entry point of the application                    |
| &nbsp;&nbsp;&nbsp;&nbsp;`index.html`                             | HTML landing page for the application             |
| &nbsp;&nbsp;&nbsp;&nbsp;`package.json`                           | Contains npm dependencies and scripts             |
| &nbsp;&nbsp;&nbsp;&nbsp;`README.md`                              | This file, contains information about the project |
| &nbsp;&nbsp;&nbsp;&nbsp;`tsconfig.json`                          | TypeScript configuration file                     |
| &nbsp;&nbsp;&nbsp;&nbsp;`vite.config.ts`                         | Vite configuration file                           |

## Setting Vue Base URL to private IP
To allow access from other devices in your private network - esp. by your mobile, you need to set the base URL of the
Vue app to your private IP address. This can be done by setting the `VITE_BASE_URL` environment variable in the
`.env.local` file in the root of the frontend module. 

Note, that your mobile must be connected to the same network as your computer running the Vue app (e.g. served by the same WLAN router).

The `.env.local` file should look like this:
```config
VITE_BASE_URL=http://your-private-ip-address:8080/api
```
To find out your private IP address, you can use the following command in the terminal (**MacOS**):
```shell
ipconfig getifaddr en0
```
For **Windows**, you can use the ipconfig command. This will display a list of all network interfaces and their IP addresses.
The IP address for the interface you're interested in (usually labeled "IPv4 Address") is your local IP address.

For **Linux**, you can use the `hostname -I` command to get your IP address.

## Project setup

```
# npm
npm install

```

### Compiles and hot-reloads for development

```
# npm
npm run dev

```

### Compiles and minifies for production

```

# npm
npm run build

```

### Customize configuration

See [Configuration Reference](https://vitejs.dev/config/).
