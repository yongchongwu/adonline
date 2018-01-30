/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { AdonlineTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MaterialDetailComponent } from '../../../../../../main/webapp/app/entities/material/material-detail.component';
import { MaterialService } from '../../../../../../main/webapp/app/entities/material/material.service';
import { Material } from '../../../../../../main/webapp/app/entities/material/material.model';

describe('Component Tests', () => {

    describe('Material Management Detail Component', () => {
        let comp: MaterialDetailComponent;
        let fixture: ComponentFixture<MaterialDetailComponent>;
        let service: MaterialService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [AdonlineTestModule],
                declarations: [MaterialDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MaterialService,
                    JhiEventManager
                ]
            }).overrideTemplate(MaterialDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MaterialDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MaterialService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Material(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.material).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
