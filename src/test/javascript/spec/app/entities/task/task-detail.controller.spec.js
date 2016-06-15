'use strict';

describe('Controller Tests', function() {

    describe('Task Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTask, MockUnit;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTask = jasmine.createSpy('MockTask');
            MockUnit = jasmine.createSpy('MockUnit');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Task': MockTask,
                'Unit': MockUnit
            };
            createController = function() {
                $injector.get('$controller')("TaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:taskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
