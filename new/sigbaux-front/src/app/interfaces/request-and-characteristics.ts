import { CharacteristicsUsage } from "./characteristics-usage";

export interface RequestAndCharacteristics {
    id: number;
    values: number;
    characteristics: CharacteristicsUsage;
}