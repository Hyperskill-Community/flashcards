import {useTheme} from "vuetify";

const currentTheme = () => {
  // change this to set the theme for the app
  return useTheme().themes.value.light;
}

const primary = () => currentTheme().colors.primary;
const secondary = () => currentTheme().colors.secondary;
const containerBackground = () => currentTheme().colors.containerBackground;

export {currentTheme, primary, secondary, containerBackground};
