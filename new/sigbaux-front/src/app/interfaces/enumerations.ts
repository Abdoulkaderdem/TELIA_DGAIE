export enum TypeBuilding2 {
  B1 = "B1: Bâtiment à usage de bureau ou d'habitation réalisé à partir de briques en terre avec chape, les enduits en ciment, la toiture en tôles et les portes et fenêtres en persiennes.",
  S1 = "S1: Bâtiment à usage de bureau ou d'habitation réalisé à partir de briques en terre sur une fondation en béton avec une chape, enduite en ciment, peinture intérieure, couverture en tôles, portes et fenêtres en persiennes.",
  S2 = "S2: Bâtiment ayant les mêmes caractéristiques que la variante S1 auxquelles s'ajoute un faux plafond.",
  S3 = "S3: Bâtiment réalisé à partir des BTG avec structure en béton armé, avec chape, enduits en ciment, peinture intérieure, couverture en tôles ou tuiles, portes et fenêtres vitrées ou en persiennes.",
  S4 = "S4: Bâtiment ayant les mêmes caractéristiques que la variante S3 auxquelles s'ajoute un faux plafond.",
  D1 = "D1: Bâtiment à usage commercial (magasin) réalisé à partir de matériaux définitifs, avec une chape, enduits en ciment, peinture intérieure, couverture en tôles, portes et fenêtres en métallique pleine.",
  D2 = "D2: Bâtiment à usage de bureau ou d'habitation réalisé à partir de matériaux définitifs, avec une chape, enduits en ciment, peinture intérieure, couverture en tôles, portes et fenêtres en persiennes.",
  D3 = "D3: Bâtiment ayant les mêmes caractéristiques que la variante D1 auxquelles s'ajoute un faux plafond.",
  D4 = "D4: Bâtiment réalisé à partir de matériaux définitifs, avec chape recouverte de gerflex, enduits en ciment, peinture intérieure, couverture en tôles avec faux plafond, portes et fenêtres vitrées.",
  D5 = "D5: Bâtiment ayant les mêmes caractéristiques que la variante D4 avec toutefois le sol en carreaux ou assimilés.",
  N1 = "N1: Bâtiment ayant les mêmes caractéristiques que ceux de la catégorie 3 (variantes D1 à D4) avec un niveau.",
  N2 = "N2: Bâtiment ayant les mêmes caractéristiques que ceux de la catégorie 3 (variantes D1 à D4) avec plusieurs niveaux."
}

export enum TypeBuilding {
  B1 = "B1",
  S1 = "S1",
  S2 = "S2",
  S3 = "S3",
  S4 = "S4",
  D1 = "D1",
  D2 = "D2",
  D3 = "D3",
  D4 = "D4",
  DM = "DM",
  N1 = "N1",
  N2 = "N2"
}

/*export enum TypeBuildingFinal {
  B1 = { code = "B1"; libelle = "Categorie I / B1"},
  S1 = "S1",
  S2 = "S2",
  S3 = "S3",
  S4 = "S4",
  D1 = "D1",
  D2 = "D2",
  D3 = "D3",
  D4 = "D4",
  D5 = "D5",
  N1 = "N1",
  N2 = "N2"
}*/

export enum ZONE2 {
  ZH = "ZONE HABITATION",
  ZCI = "ZONE COMMERCIAL ET INDUSTRIELLE",
  ZRA = "ZONE RESIDENTIELLE ET ADMINISTRATIVE"
}

export enum ZONE {
  ZH = "ZH",
  ZCI = "ZCI",
  ZRA = "ZRA"
}


export enum BUILDING_STATUS {
  MATCHED = "MATCHED",
  NO_CONFORM = "NO_CONFORM"
}


export enum LOCALITY2 {
  OUAGA = "OUAGADOUGOU",
  BOBO = "BOBO DIOULASSO",
  CHEF_LIEUX = "CHEFS-LIEUX DE REGIONS",
  AUTRES = "AUTRES LOCALITES"
}

export enum LOCALITY {
  OUAGA = "OUAGA",
  BOBO = "BOBO",
  CHEF_LIEUX = "CHEF_LIEUX",
  AUTRES = "AUTRES"
}

export enum TypePropertyTitle {
  LAND_TITLE = 'LAND_TITLE',
  PUH = 'PUH',
  ATTRIBUTION_CERTIFICATE = 'ATTRIBUTION_CERTIFICATE'
}

export enum TypePropertyTitle2 {
  LAND_TITLE = 'LAND_TITLE',
  PUH = 'PUH',
  ATTRIBUTION_CERTIFICATE = 'ATTRIBUTION_CERTIFICATE'
}

export enum BuildingStanding {
  PARKING = 'PARKING',
  PAVE = 'PAVE',
  GARDEN = 'GARDEN',
  SWIMMING_POOL = 'SWIMMING_POOL',
  MEETING_ROOM = 'MEETING_ROOM',
  OTHER = 'OTHER'
}

export enum TypeLandLord {
  INDIVIDUAL = 'INDIVIDUAL',
  LEGAL_ENTITY = 'LEGAL_ENTITY'
}

export enum QualityApplicant {
  OWNER = 'OWNER',
  MANDATARY = 'MANDATARY'
}

export enum RentalStatus {
  AVAILABLE = 'AVAILABLE',
  RENTED = 'RENTED',
  DISABLED = 'DISABLED',
  ENABLE = 'ENABLE',
  INPROGRESS = 'INPROGRESS',
  CANCELED = 'CANCELED',
  VALIDATED = 'VALIDATED'
}

export enum TypeUser {
  PUBLIC = 'PUBLIC',
  DGAIE = 'DGAIE',
  ADMIN = 'ADMIN',
  USER = 'USER',
  GUEST = 'GUEST'
}

export enum RoleType {
  DSI = 'DSI',
  CPM = 'CPM',
  ORDON = 'ORDON',
  ADMIN = 'ADMIN',
  DAIE = 'DAIE',
  DGAHCMAH = 'DGAHCMAH',
  CNOI = 'CNOI',
  BAILLEUR = 'BAILLEUR',
  SUPER_ADMIN = 'SUPER_ADMIN',
  EDITOR = 'EDITOR',
  VIEWER = 'VIEWER'
}

export class EnumUtils {
  public static getEnumValues(enumType: any): string[] {
    return Object.keys(enumType).map(key => enumType[key]);
  }
}