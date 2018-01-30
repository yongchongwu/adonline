import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AdonlineMaterialModule } from './material/material.module';
import { AdonlineAdvertiserRequirementModule } from './advertiser-requirement/advertiser-requirement.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        AdonlineMaterialModule,
        AdonlineAdvertiserRequirementModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdonlineEntityModule {}
