
// Type definitions for react-native-share-menu  v1.4.0
// Project: https://github.com/meedan/react-native-share-menu
// Definitions by: Tal <https://github.com/taljacobson>
// TypeScript Version: 2.1

declare module "react-native-share-menu" { 
    export default class ShareMenu {
         static getSharedText(CB: (text:string) => any): void
         static clearSharedText(): void
    } 
}