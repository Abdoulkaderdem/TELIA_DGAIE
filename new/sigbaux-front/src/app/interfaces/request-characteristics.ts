import { CharacteristicsUsage } from "./characteristics-usage";

export interface RequestCharacteristics {
    id: number;
    values: number;
    characteristics: CharacteristicsUsage;
}